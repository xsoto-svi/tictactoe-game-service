package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.constant.ErrorMessage;
import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends ApiException {
  public RoomNotFoundException() {
    super(HttpStatus.NOT_FOUND, ErrorMessage.ROOM_NOT_FOUND.getMessage());
  }
}
