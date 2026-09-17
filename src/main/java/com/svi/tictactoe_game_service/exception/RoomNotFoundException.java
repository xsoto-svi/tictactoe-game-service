package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.enums.ErrorMessage;
import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends RuntimeException {
  public RoomNotFoundException() {
    super(ErrorMessage.ROOM_NOT_FOUND.getMessage());
  }
}
