package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.enums.ErrorMessage;

public class NameAlreadyTakenException extends RuntimeException {
  public NameAlreadyTakenException() {
    super(ErrorMessage.NAME_ALREADY_TAKEN.getMessage());
  }
}
