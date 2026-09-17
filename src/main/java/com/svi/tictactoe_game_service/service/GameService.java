package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.room.SaveMoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGameResponse;

import java.util.UUID;

public interface GameService {
  void addMove(UUID gameId, SaveMoveRequest request);
  GetGameResponse getMovesByGameId(UUID gameId);
}
