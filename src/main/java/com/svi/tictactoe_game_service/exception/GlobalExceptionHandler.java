package com.svi.tictactoe_game_service.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.svi.tictactoe_game_service.dto.response.ErrorResponse;
import com.svi.tictactoe_game_service.enums.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // Handles any general database error
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponse> handleDatabaseException(DataAccessException ex) {
    log.error("error: ", ex);
    ErrorResponse response = new ErrorResponse(ErrorMessage.DATABASE_ERROR.getMessage());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    // Extracting just the raw message from the first violation
    String cleanMessage = ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .findFirst()
            .orElse("Validation failed");

    ErrorResponse response = new ErrorResponse(cleanMessage);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    log.error("error: ", ex);
    String paramName = ex.getName();
    String cleanMessage = ErrorMessage.TYPE_MISMATCH.formatMessage(paramName);

    if (ex.getRequiredType() != null && ex.getRequiredType().isAssignableFrom(UUID.class)) {
      cleanMessage = ErrorMessage.UUID_TYPE_MISMATCH.formatMessage(paramName);
    }

    ErrorResponse response = new ErrorResponse(cleanMessage);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleDtoValidation(MethodArgumentNotValidException ex) {
    log.error("error: ", ex);

    FieldError fieldError = ex.getBindingResult().getFieldError();
    String cleanMessage = "Validation failed";

    if (fieldError != null) {
      cleanMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    ErrorResponse response = new ErrorResponse(cleanMessage);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  // Handles malformed JSON bodies
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    log.error("error: ", ex);

    String cleanMessage = "Malformed JSON request body.";

    if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
      if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isAssignableFrom(UUID.class)) {
        cleanMessage = ErrorMessage.INVALID_UUID_REQUEST.getMessage();
      }
    }

    ErrorResponse response = new ErrorResponse(cleanMessage);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(NameAlreadyTakenException.class)
  public ResponseEntity<ErrorResponse> handleNameAlreadyTaken(NameAlreadyTakenException ex) {
    log.error("error: ", ex);
    ErrorResponse response = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(InvalidGameException.class)
  public ResponseEntity<ErrorResponse> handleInvalidGame(InvalidGameException ex) {
    log.error("error: ", ex);
    ErrorResponse response = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), ex);
    ErrorResponse response = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}