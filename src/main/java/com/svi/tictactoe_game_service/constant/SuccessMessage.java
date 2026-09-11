package com.svi.tictactoe_game_service.constant;

import lombok.Getter;

@Getter
public enum SuccessMessage {
  CREATED_GAME("Successfully created game"),
  JOIN_GAME("Successfully created game"),
  REMATCH("Successfully started a rematch");

  private final String message;

  SuccessMessage(String message) { this.message = message; }
}
