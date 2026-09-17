package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.enums.ErrorMessage;
import com.svi.tictactoe_game_service.enums.MoveError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidMoveException extends RuntimeException {
  private final MoveError error;

  public InvalidMoveException(MoveError error, Object... args) {
    super(error.format(args));
    this.error = error;
  }
}
