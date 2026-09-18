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
import com.svi.tictactoe_game_service.exception.InvalidGameException;
import com.svi.tictactoe_game_service.exception.NameAlreadyTakenException;
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
  @Override
  public MatchMakingResponse createGame(CreateGameRequest request) {
    String roomCode = generateUniqueRoomCode();
    UUID gameId = generateGameId();
    saveNewRoom(roomCode, gameId, GameStatus.WAITING);
    saveNewPlayer(request.name(), gameId, roomCode);

    return new MatchMakingResponse(PlayerSymbol.X, gameId, roomCode);
  }

  // returns player symbol
  @Override
  public MatchMakingResponse joinGame(String roomCode, JoinGameRequest request) {
    Room room = findRoom(roomCode);

    GameStatus status = room.getStatus();
    PlayerSymbol symbol;

    if (status == GameStatus.WAITING) {
      if (playerRepository.existsByGameIdAndName(room.getGameId(), request.name())){
        throw new NameAlreadyTakenException();
      }

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

  @Override
  public GameStatusResponse checkRoomStatus(String roomCode) {
    Room room = findRoom(roomCode);

    return new GameStatusResponse(room.getStatus());
  }

  @Override
  public RematchResponse rematchGame(String roomCode, RematchGameRequest request) {
    Room knownRoom = roomRepository.findByRoomCodeAndGameId(roomCode, request.gameId());

    if (knownRoom == null) {
      throw new InvalidGameException();
    }

    if (knownRoom.getStatus() == GameStatus.GAME_OVER) {
      // --- FIRST PLAYER REQUESTS REMATCH ---
      knownRoom.setStatus(GameStatus.CLOSED);
      roomRepository.save(knownRoom);

      UUID newGameId = generateGameId();
      saveNewRoom(roomCode, newGameId, GameStatus.WAITING);
      saveNewPlayer(request.name(), newGameId, roomCode);

      return new RematchResponse(newGameId);

    } else if (knownRoom.getStatus() == GameStatus.CLOSED) {
      // --- SECOND PLAYER ACCEPTS REMATCH ---
      // Player 2 sent the old ID, so we must search the room to find the new WAITING game
      List<Room> rooms = roomRepository.findByRoomCode(roomCode);

      Room waitingRoom = rooms.stream()
              .filter(room -> room.getStatus() == GameStatus.WAITING)
              .findFirst()
              .orElseThrow(InvalidGameException::new); // No rematch was actually started

      waitingRoom.setStatus(GameStatus.IN_PROGRESS);
      roomRepository.save(waitingRoom);

      saveNewPlayer(request.name(), waitingRoom.getGameId(), roomCode);

      return new RematchResponse(waitingRoom.getGameId());

    } else {
      throw new InvalidGameException();
    }
  }

  @Override
  public void leaveGame(String roomCode, LeaveGameRequest request) {
    Room room = roomRepository.findByRoomCodeAndGameId(roomCode, request.gameId());

    if (room == null) {
      throw new RoomNotFoundException();
    }

    if (room.getStatus() != GameStatus.CLOSED && room.getStatus() != GameStatus.CANCELLED) {
      room.setStatus(GameStatus.CLOSED);
      roomRepository.save(room);
    }
  }

  @Override
  public GetRoomsResponse getRooms() {
    List<Room> rooms = roomRepository.findAll();

    List<String> roomCodes = rooms.stream()
            .map(Room::getRoomCode)
            .distinct()
            .toList();

    return new GetRoomsResponse(roomCodes);
  }

  @Override
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
    player.setName(playerName);
    player.setGameId(gameId);
    player.setRoomCode(roomCode);

    playerRepository.save(player);
  }
}
