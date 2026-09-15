package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.entity.Player;
import com.svi.tictactoe_game_service.enums.ErrorMessage;
import com.svi.tictactoe_game_service.enums.GameStatus;
import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import com.svi.tictactoe_game_service.enums.SuccessMessage;
import com.svi.tictactoe_game_service.dto.request.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.request.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.entity.Room;
import com.svi.tictactoe_game_service.exception.RoomNotFoundException;
import com.svi.tictactoe_game_service.repository.MoveRepository;
import com.svi.tictactoe_game_service.repository.PlayerRepository;
import com.svi.tictactoe_game_service.repository.RoomRepository;
import com.svi.tictactoe_game_service.service.GameService;
import com.svi.tictactoe_game_service.util.EntityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

  private MoveRepository moveRepository;
  private RoomRepository roomRepository;
  private PlayerRepository playerRepository;

  private final List<String> board;
  private int spectatorCount;
  private GameStatus gameStatus;

  public GameServiceImpl(
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

  public MatchMakingResponse createGame(CreateGameRequest request) {
    UUID gameId = generateGameId();
    saveRoom(request, gameId);
    savePlayer(request, gameId);

    return new MatchMakingResponse(PlayerSymbol.X);

  }

  public MatchMakingResponse joinGame(JoinGameRequest request) {
    Room room = findRoom(request.roomCode());
    GameStatus status = room.getStatus();
    PlayerSymbol symbol = request.symbol();

    if (status == GameStatus.WAITING) {
      room.setStatus(GameStatus.IN_PROGRESS);
      symbol = PlayerSymbol.O;

    } else if (status == GameStatus.IN_PROGRESS || status == GameStatus.REMATCH_WAITING) {
      spectatorCount++;
      symbol = PlayerSymbol.SPECTATOR;

      // prevents
    } else if (status == GameStatus.REMATCH_WAITING && symbol == PlayerSymbol.O) {
      room.setStatus(GameStatus.IN_PROGRESS);
      roomRepository.save(room);

    } else {
      throw new RoomNotFoundException();
    }

    if (symbol != PlayerSymbol.SPECTATOR) {
      roomRepository.save(room);
    }

    return new MatchMakingResponse(symbol);
  }

  public GameStatus checkGameStatus() {
    return this.gameStatus;
  }

  public List<String> checkBoardState() {

  }

  public String rematchGame(JoinGameRequest request) {
    Room room = findRoom(request.roomCode());

    if (gameStatus == GameStatus.REMATCH_WAITING) {
      room.setStatus(GameStatus.IN_PROGRESS);
      roomRepository.save(room);
    }

    return "placeholder";
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
