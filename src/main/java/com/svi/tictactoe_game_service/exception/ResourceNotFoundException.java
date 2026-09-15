package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.enums.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
  public ResourceNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, ErrorMessage.ROOM_NOT_FOUND.getMessage());
  }
}
