package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.enums.GameStatus;
import com.svi.tictactoe_game_service.dto.request.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.response.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;

import java.util.List;

public interface GameService {
  MatchMakingResponse createGame(CreateGameRequest request);
  MatchMakingResponse joinGame(JoinGameRequest request);
  GameStatus checkGameStatus();
  List<String> checkBoardState();
  String rematchGame();
  void leaveGame();
}
