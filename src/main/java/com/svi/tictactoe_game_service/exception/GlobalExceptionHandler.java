package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // Handles any general database error
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponse> handleDatabaseException(DataAccessException ex) {
    log.error("error: ", ex);
    ErrorResponse response = new ErrorResponse("A database error occurred. Please try again later.");
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }

  @ExceptionHandler(RoomNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRoomNotFoundException(RoomNotFoundException ex) {
    log.error("error: ", ex);
    ErrorResponse response = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(InvalidMoveException.class)
  public ResponseEntity<ErrorResponse> handleInvalidMoveException(InvalidMoveException ex) {
    log.error("error: ", ex);
    ErrorResponse response = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}