package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.constant.GameStatus;
import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import com.svi.tictactoe_game_service.constant.SuccessMessage;
import com.svi.tictactoe_game_service.exception.RoomNotFoundException;
import com.svi.tictactoe_game_service.model.dto.request.CreateGameRequest;
import com.svi.tictactoe_game_service.model.dto.request.LeaveGameRequest;
import com.svi.tictactoe_game_service.repository.GameRepository;
import com.svi.tictactoe_game_service.service.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

  private final List<String> board;
  private int spectatorCount;
  private GameStatus gameStatus;

  public GameServiceImpl() {
    this.board = new ArrayList<>(Collections.nCopies(9, ""));
    this.spectatorCount = 0;
  }

  public PlayerSymbol createGame(CreateGameRequest createGameRequest) {
    gameStatus = GameStatus.WAITING;
    return PlayerSymbol.X;
  }

  public PlayerSymbol joinGame(CreateGameRequest createGameRequest) {
    if (gameStatus == GameStatus.WAITING) {
      return PlayerSymbol.O;
    } else if (gameStatus == GameStatus.IN_PROGRESS) {
      spectatorCount++;
      return PlayerSymbol.SPECTATOR;
    }

    throw new RoomNotFoundException();
  }

  public GameStatus checkGameStatus() {
    return this.gameStatus;
  }

  public List<String> checkBoardState() {
    return this.board;
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
}
