package com.svi.tictactoe_game_service.dto.response.game;

import java.util.List;

public record GetGameResponse(
        List<MoveDto> moves
) {
  public record MoveDto(
          String name,
          String symbol,
          int location
  ) {}
}
