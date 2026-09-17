package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.enums.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(ErrorMessage.ROOM_NOT_FOUND.getMessage());
  }
}
