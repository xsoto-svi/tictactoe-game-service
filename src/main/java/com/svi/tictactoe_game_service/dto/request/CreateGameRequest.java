package com.svi.tictactoe_game_service.dto.response;

import jakarta.validation.constraints.NotBlank;

public class CreateGameRequest {
  @NotBlank(message = "Room code cannot be empty")
  private String roomCode;
}
