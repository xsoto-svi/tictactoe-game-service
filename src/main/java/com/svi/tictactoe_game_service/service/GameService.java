package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import com.svi.tictactoe_game_service.model.dto.request.CreateGameRequest;

public interface GameService {
  PlayerSymbol createGame(CreateGameRequest createGameRequest);
  PlayerSymbol joinGame(CreateGameRequest createGameRequest);
}
