package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.dto.request.room.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.dto.response.game.GameStatusResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;
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
  public MatchMakingResponse createGame(String roomCode, CreateGameRequest request) {
    UUID gameId = generateGameId();
    saveNewRoom(roomCode, gameId, GameStatus.WAITING);
    saveNewPlayer(request.playerName(), gameId, roomCode);

    return new MatchMakingResponse(PlayerSymbol.X, gameId);
  }

  // returns player symbol
  public MatchMakingResponse joinGame(String roomCode, JoinGameRequest request) {
    Room room = findRoom(roomCode);

    GameStatus status = room.getStatus();
    PlayerSymbol symbol = request.symbol();

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

    return new MatchMakingResponse(symbol, room.getGameId());
  }

  public GameStatusResponse checkGameStatus(String roomCode) {
    Room room = findRoom(roomCode);

    return new GameStatusResponse(room.getStatus());
  }

  public void rematchGame(String roomCode, JoinGameRequest request) {
    Room room = findRoom(roomCode);
    String name = request.name();

    GameStatus status = room.getStatus();

    if (status == GameStatus.REMATCH_WAITING) {
      // second player accepts

      // Close current game
      room.setStatus(GameStatus.CLOSED);
      roomRepository.save(room);

      saveNewRoom(roomCode, generateGameId(), GameStatus.IN_PROGRESS);

      List<Player> oldPlayers = playerRepository.findByGameId(room.getGameId());

      for (Player player : oldPlayers) {
        saveNewPlayer(player.getPlayerName(), newGameId, roomCode);
      }
    } else {
      // first player starts a rematch
      room.setStatus(GameStatus.REMATCH_WAITING);
      roomRepository.save(room);
      playerRepository.save()
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
