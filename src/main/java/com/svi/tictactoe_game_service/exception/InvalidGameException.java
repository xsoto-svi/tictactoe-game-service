package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.enums.ErrorMessage;

public class InvalidGameException extends RuntimeException {
  public InvalidGameException() {
    super(ErrorMessage.INVALID_GAME.getMessage());
  }
}
