package com.svi.tictactoe_game_service.model.dto.response;

import com.svi.tictactoe_game_service.constant.GameStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GameStatusResponse extends ApiResponse{
  private final GameStatus gameStatus;

  public GameStatusResponse(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }
}
