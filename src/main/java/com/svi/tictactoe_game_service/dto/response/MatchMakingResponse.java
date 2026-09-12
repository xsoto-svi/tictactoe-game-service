package com.svi.tictactoe_game_service.model.dto.response;

import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

public class MatchMakingResponse {
  private PlayerSymbol playerSymbol;
}