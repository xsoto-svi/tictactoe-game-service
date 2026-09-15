package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.enums.ErrorMessage;
import com.svi.tictactoe_game_service.enums.GameStatus;
import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import com.svi.tictactoe_game_service.enums.SuccessMessage;
import com.svi.tictactoe_game_service.dto.request.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.response.CreateGameRequest;
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

@Service
public class GameServiceImpl implements GameService {

  private MoveRepository moveRepository;
  private RoomRepository roomRepository;
  private PlayerRepository playerRepository;

  public GameServiceImpl(
          MoveRepository moveRepository,
          RoomRepository roomRepository,
          PlayerRepository playerRepository
  ) {
    this.moveRepository = moveRepository;
    this.roomRepository = roomRepository;
    this.playerRepository = playerRepository;
  }

  private List<String> board;
  private int spectatorCount;
  private GameStatus gameStatus;

  public GameServiceImpl() {
    this.board = new ArrayList<>(Collections.nCopies(9, ""));
    this.spectatorCount = 0;
  }

  public MatchMakingResponse createGame(CreateGameRequest request) {
    gameStatus = GameStatus.WAITING; // repository call

    return new MatchMakingResponse(PlayerSymbol.X);

  }

  public MatchMakingResponse joinGame(JoinGameRequest request) {
    Room room = findRoom(request.roomCode());
    GameStatus status = room.getStatus();
    PlayerSymbol symbol = request.symbol();

    if (status == GameStatus.WAITING) {
      room.setStatus(GameStatus.IN_PROGRESS);
      roomRepository.save(room);
      return new MatchMakingResponse(PlayerSymbol.O);

    } else if (status == GameStatus.IN_PROGRESS) {
      spectatorCount++;
      return new MatchMakingResponse(PlayerSymbol.SPECTATOR);

    } else if (status == GameStatus.REMATCH_WAITING && symbol != PlayerSymbol.SPECTATOR) {
      room.setStatus(GameStatus.IN_PROGRESS);

    } else {
      spectatorCount++;
      return new MatchMakingResponse(PlayerSymbol.SPECTATOR);
    }

    throw new RoomNotFoundException();
  }

  public GameStatus checkGameStatus() {
    return this.gameStatus;
  }

  public List<String> checkBoardState() {

  }

  public String rematchGame() {
    if (gameStatus == GameStatus.REMATCH_WAITING) {
      gameStatus = GameStatus.IN_PROGRESS;
      resetGame();
      return SuccessMessage.REMATCH.getMessage();
    }

    return GameStatus.REMATCH_WAITING.toString();
  }

  public void resetGame() {
    this.board.clear();
  }

  public void leaveGame(LeaveGameRequest leaveGameRequest) {
    if (leaveGameRequest.getPlayerSymbol() == PlayerSymbol.SPECTATOR) {
      spectatorCount--;
    }
  }

  private Room findRoom(String roomId) {
    return EntityUtil.getOrThrow(roomRepository.findById(roomId), ErrorMessage.ROOM_NOT_FOUND.getMessage());
  }
}
