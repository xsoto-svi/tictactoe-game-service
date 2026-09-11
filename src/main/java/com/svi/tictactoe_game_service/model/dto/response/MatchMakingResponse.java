package com.svi.tictactoe_game_service.model.dto.response;

import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MatchMakingResponse extends ApiResponse {
  private final PlayerSymbol playerSymbol;

  public MatchMakingResponse(String message, PlayerSymbol playerSymbol) {
    super(message);
    this.playerSymbol = playerSymbol;
  }
}