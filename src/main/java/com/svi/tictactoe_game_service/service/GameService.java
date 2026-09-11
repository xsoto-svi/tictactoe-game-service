package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.constant.GameStatus;
import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import com.svi.tictactoe_game_service.model.dto.request.CreateGameRequest;

import java.util.List;

public interface GameService {
  PlayerSymbol createGame(CreateGameRequest createGameRequest);
  PlayerSymbol joinGame(CreateGameRequest createGameRequest);
  GameStatus checkGameStatus();
  List<String> checkBoardState();
  String rematchGame();
  void leaveGame();
}
