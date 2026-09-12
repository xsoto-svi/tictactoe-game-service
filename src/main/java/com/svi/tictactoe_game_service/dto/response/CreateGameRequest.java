package com.svi.tictactoe_game_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGameRequest {

  public static final String NAME_BLANK_ERROR = "Room code cannot be empty";

  @NotBlank(message = NAME_BLANK_ERROR)
  private String roomCode;
}
