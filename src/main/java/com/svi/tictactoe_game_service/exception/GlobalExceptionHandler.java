package com.svi.tictactoe_game_service.exception;

import com.svi.tictactoe_game_service.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentMismatch(MethodArgumentTypeMismatchException ex) {
    String paramName = ex.getName();
    Object providedValue = ex.getValue();
    Class<?> requiredType = ex.getRequiredType();

    String typeName = (requiredType != null) ? requiredType.getSimpleName() : "valid format";

    String message = String.format(
            "Failed to convert parameter '%s' with value '%s' to required type '%s'",
            paramName, providedValue, typeName
    );

    log.warn("Path/Query variable conversion error: {}", message);

    ErrorResponse response = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), ex);
    ErrorResponse response = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}