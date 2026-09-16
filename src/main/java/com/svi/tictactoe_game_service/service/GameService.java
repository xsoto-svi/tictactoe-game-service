package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.SaveMoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGameResponse;

import java.util.UUID;

public interface GameService {
  void saveMove(UUID gameId, SaveMoveRequest request);
  GetGameResponse getMovesByGameId(UUID gameId);
}
