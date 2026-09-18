package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.game.SaveMoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGamesByGameIdResponse;

import java.util.UUID;

public interface GameService {
  void addMove(UUID gameId, SaveMoveRequest request);
  GetGamesByGameIdResponse getMovesByGameId(UUID gameId);
}
