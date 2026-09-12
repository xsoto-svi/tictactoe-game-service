package com.svi.tictactoe_game_service.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Handles any general database error (including Cassandra save/query failures)
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<> handleDatabaseException(DataAccessException ex) {
    // Log the actual error internally for debugging
    // log.error("Database error occurred: ", ex);

    ApiResponse response = new ApiResponse("A database error occurred. Please try again later.");
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }
}