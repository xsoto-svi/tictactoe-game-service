package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.dto.request.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.request.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.entity.Player;
import com.svi.tictactoe_game_service.entity.Room;
import com.svi.tictactoe_game_service.enums.ErrorMessage;
import com.svi.tictactoe_game_service.enums.GameStatus;
import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import com.svi.tictactoe_game_service.exception.RoomNotFoundException;
import com.svi.tictactoe_game_service.repository.MoveRepository;
import com.svi.tictactoe_game_service.repository.PlayerRepository;
import com.svi.tictactoe_game_service.repository.RoomRepository;
import com.svi.tictactoe_game_service.service.RoomService;
import com.svi.tictactoe_game_service.util.EntityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class RoomServiceImpl implements RoomService {

  private MoveRepository moveRepository;
  private RoomRepository roomRepository;
  private PlayerRepository playerRepository;

  private final List<String> board;
  private int spectatorCount;
  private GameStatus gameStatus;

  public RoomServiceImpl(
          MoveRepository moveRepository,
          RoomRepository roomRepository,
          PlayerRepository playerRepository
  ) {
    this.moveRepository = moveRepository;
    this.roomRepository = roomRepository;
    this.playerRepository = playerRepository;

    this.board = new ArrayList<>(Collections.nCopies(9, ""));
    this.spectatorCount = 0;
  }

  // returns player symbol
  public MatchMakingResponse createGame(CreateGameRequest request) {
    UUID gameId = generateGameId();
    saveRoom(request, gameId);
    savePlayer(request, gameId);

    return new MatchMakingResponse(PlayerSymbol.X);

  }

  // returns player symbol
  public MatchMakingResponse joinGame(JoinGameRequest request) {
    Room room = findRoom(request.roomCode());
    Player player = new Player();

    GameStatus status = room.getStatus();
    PlayerSymbol symbol = request.symbol();

    player.setPlayerName(request.name());
    player.setGameId(room.getGameId());
    player.setRoomCode(request.roomCode());

    if (status == GameStatus.WAITING) {
      room.setStatus(GameStatus.IN_PROGRESS);
      symbol = PlayerSymbol.O;

    } else if (status != GameStatus.CANCELLED && status != GameStatus.CLOSED) {
      spectatorCount++;
      symbol = PlayerSymbol.SPECTATOR;

    } else {
      throw new RoomNotFoundException();
    }

    if (symbol != PlayerSymbol.SPECTATOR) {
      playerRepository.save(player);
      roomRepository.save(room);
    }

    return new MatchMakingResponse(symbol);
  }

  public GameStatus checkGameStatus() {
    return this.gameStatus;
  }

  public List<String> checkBoardState() {

  }

  public void rematchGame(JoinGameRequest request) {
    Room currRoom = findRoom(request.roomCode());

    GameStatus status = currRoom.getStatus();
    PlayerSymbol currPlayerSymbol = request.symbol()

    if (status == GameStatus.REMATCH_WAITING && currPlayerSymbol == PlayerSymbol.O) {
      currRoom.setStatus(GameStatus.IN_PROGRESS);
      currRoom.setGameId(generateGameId());
      roomRepository.save(currRoom);
    }
  }

  public void resetGame() {
    this.board.clear();
  }

  public void leaveGame(LeaveGameRequest request) {
    if (request.getPlayerSymbol() == PlayerSymbol.SPECTATOR) {
      spectatorCount--;
    }
  }

  // UTILS
  private Room findRoom(String roomId) {
    return EntityUtil.getOrThrow(roomRepository.findById(roomId), ErrorMessage.ROOM_NOT_FOUND.getMessage());
  }

  private UUID generateGameId() {
    return UUID.randomUUID();
  }

  private void saveRoom(CreateGameRequest request, UUID gameId) {
    Room room = new Room();
    room.setRoomCode(request.roomCode());
    room.setGameId(generateGameId());
    room.setStatus(GameStatus.WAITING);

    roomRepository.save(room);
  }

  private void savePlayer(CreateGameRequest request, UUID gameId) {
    Player player = new Player();
    player.setPlayerName(request.playerName());
    player.setGameId(gameId);
    player.setRoomCode(request.roomCode());
  }
}
