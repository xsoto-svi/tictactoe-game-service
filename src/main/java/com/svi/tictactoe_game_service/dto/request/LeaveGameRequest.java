package com.svi.tictactoe_game_service.dto.request;

import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveGameRequest {

  public static final String PLAYER_SYMBOL_NULL_ERROR = "Player symbol cannot be empty";

  @NotNull(message = PLAYER_SYMBOL_NULL_ERROR)
  private PlayerSymbol playerSymbol;
}
