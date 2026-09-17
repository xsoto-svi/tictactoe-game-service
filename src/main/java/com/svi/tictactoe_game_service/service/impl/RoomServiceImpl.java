package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.dto.request.room.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.RematchGameRequest;
import com.svi.tictactoe_game_service.dto.response.room.MatchMakingResponse;
import com.svi.tictactoe_game_service.dto.response.game.GameStatusResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;
import com.svi.tictactoe_game_service.dto.response.room.RematchResponse;
import com.svi.tictactoe_game_service.entity.Player;
import com.svi.tictactoe_game_service.entity.Room;
import com.svi.tictactoe_game_service.enums.GameStatus;
import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import com.svi.tictactoe_game_service.exception.RoomNotFoundException;
import com.svi.tictactoe_game_service.repository.PlayerRepository;
import com.svi.tictactoe_game_service.repository.RoomRepository;
import com.svi.tictactoe_game_service.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;
  private final PlayerRepository playerRepository;

  public RoomServiceImpl(
          RoomRepository roomRepository,
          PlayerRepository playerRepository
  ) {
    this.roomRepository = roomRepository;
    this.playerRepository = playerRepository;
  }

  // returns player symbol
  public MatchMakingResponse createGame(CreateGameRequest request) {
    String roomCode = generateUniqueRoomCode();
    UUID gameId = generateGameId();
    saveNewRoom(roomCode, gameId, GameStatus.WAITING);
    saveNewPlayer(request.name(), gameId, roomCode);

    return new MatchMakingResponse(PlayerSymbol.X, gameId, roomCode);
  }

  // returns player symbol
  public MatchMakingResponse joinGame(String roomCode, JoinGameRequest request) {
    Room room = findRoom(roomCode);

    GameStatus status = room.getStatus();
    PlayerSymbol symbol;

    if (status == GameStatus.WAITING) {
      room.setStatus(GameStatus.IN_PROGRESS);
      symbol = PlayerSymbol.O;

    } else if (status != GameStatus.CANCELLED && status != GameStatus.CLOSED) {
      symbol = PlayerSymbol.SPECTATOR;

    } else {
      throw new RoomNotFoundException();
    }

    if (symbol != PlayerSymbol.SPECTATOR) {
      saveNewPlayer(request.name(), room.getGameId(), roomCode);
      roomRepository.save(room);
    }

    return new MatchMakingResponse(symbol, room.getGameId(), roomCode);
  }

  public GameStatusResponse checkGameStatus(String roomCode) {
    Room room = findRoom(roomCode);

    return new GameStatusResponse(room.getStatus());
  }

  public RematchResponse rematchGame(String roomCode, RematchGameRequest request) {
    Room currRoom = findRoom(roomCode);
    GameStatus status = currRoom.getStatus();

    if (status == GameStatus.REMATCH_WAITING) {
      // --- SECOND PLAYER ACCEPTS ---
      UUID newGameId = generateGameId();

      currRoom.setStatus(GameStatus.CLOSED);
      roomRepository.save(currRoom);

      saveNewRoom(roomCode, newGameId, GameStatus.IN_PROGRESS);

      // Add new game to current players
      List<Player> oldPlayers = playerRepository.findAllByGameId(currRoom.getGameId());
      for (Player oldPlayer : oldPlayers) {
        saveNewPlayer(oldPlayer.getPlayerName(), newGameId, roomCode);
      }

      return new RematchResponse(newGameId);

    } else {
      // --- FIRST PLAYER REQUESTS REMATCH ---

      // Update old room status
      currRoom.setStatus(GameStatus.REMATCH_WAITING);
      roomRepository.save(currRoom);

      // Returns old id
      // Player 1 should poll to get the new id when player 2 accepts
      return new RematchResponse(currRoom.getGameId());
    }
  }

  public void leaveGame(String roomCode, LeaveGameRequest request) {
    Room room = findRoom(roomCode);

    if (room.getStatus() != GameStatus.CLOSED){
      room.setStatus(GameStatus.CLOSED);
      roomRepository.save(room);
    }
  }

  public GetRoomsResponse getRooms() {
    List<Room> rooms = roomRepository.findAll();

    List<String> roomCodes = rooms.stream()
            .map(Room::getRoomCode)
            .toList();

    return new GetRoomsResponse(roomCodes);
  }

  public GetGamesByRoomResponse getGamesByRoomCode(String roomCode) {
    List<Room> rooms = roomRepository.findByRoomCode(roomCode);

    List<UUID> gameIds = rooms.stream()
            .map(Room::getGameId)
            .toList();

    return new GetGamesByRoomResponse(gameIds);
  }

  // UTILS
  private Room findRoom(String roomCode) {
    List<Room> rooms = roomRepository.findByRoomCode(roomCode);

    if (rooms == null || rooms.isEmpty()) {
      throw new RoomNotFoundException();
    }

    // Find the single active game in the room's history
    return rooms.stream()
            .filter(room -> room.getStatus() != GameStatus.CLOSED
                    && room.getStatus() != GameStatus.CANCELLED)
            .findFirst()
            .orElseThrow(RoomNotFoundException::new);
  }

  private UUID generateGameId() {
    return UUID.randomUUID();
  }

  private String generateUniqueRoomCode() {
    String roomCode;
    do {
      roomCode = generateRandom4CharCode();
    } while (isRoomCodeInUse(roomCode));

    return roomCode;
  }

  private String generateRandom4CharCode() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder code = new StringBuilder();
    java.util.Random rnd = new java.util.Random();

    while (code.length() < 4) {
      int index = (int) (rnd.nextFloat() * chars.length());
      code.append(chars.charAt(index));
    }
    return code.toString();
  }

  private boolean isRoomCodeInUse(String roomCode) {
    List<Room> rooms = roomRepository.findByRoomCode(roomCode);

    if (rooms == null || rooms.isEmpty()) {
      return false;
    }

    // If the code exists, check if there is currently an active game using it.
    // If all past games with this code are CLOSED, we are free to recycle it safely.
    return rooms.stream()
            .anyMatch(room -> room.getStatus() != GameStatus.CLOSED
                    && room.getStatus() != GameStatus.CANCELLED);
  }

  private void saveNewRoom(String roomCode, UUID gameId, GameStatus status) {
    Room room = new Room();
    room.setRoomCode(roomCode);
    room.setGameId(gameId);
    room.setStatus(status);

    roomRepository.save(room);
  }

  private void saveNewPlayer(String playerName, UUID gameId, String roomCode) {
    Player player = new Player();
    player.setPlayerName(playerName);
    player.setGameId(gameId);
    player.setRoomCode(roomCode);

    playerRepository.save(player);
  }
}
