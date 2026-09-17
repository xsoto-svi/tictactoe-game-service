package com.svi.tictactoe_game_service.enums;

import lombok.Getter;

@Getter
public enum MoveError {
  BOARD_FULL("The board is already full."),
  LOCATION_OCCUPIED("Location %d is already occupied."),
  GAME_ALREADY_FINISHED("The game has already ended."),
  NOT_YOUR_TURN("It is not your turn."),
  X_MUST_START("Player 'X' must make the first move.");

  private final String message;

  MoveError(String message) { this.message = message; }

  public String format(Object... args) {
    return String.format(message, args);
  }
}
