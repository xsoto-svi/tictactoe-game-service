package com.svi.tictactoe_game_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateGameRequest(
  @NotBlank(message = "Room code cannot be empty")
  String roomCode,

  @NotBlank(message = "Name cannot be empty")
  String name
) {
}
