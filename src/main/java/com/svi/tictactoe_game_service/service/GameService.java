package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.game.MoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGamesByGameIdResponse;
import com.svi.tictactoe_game_service.dto.response.game.MoveResponse;

import java.util.UUID;

public interface GameService {
  MoveResponse processMove(UUID gameId, MoveRequest request);
  GetGamesByGameIdResponse getMovesByGameId(UUID gameId);
}
