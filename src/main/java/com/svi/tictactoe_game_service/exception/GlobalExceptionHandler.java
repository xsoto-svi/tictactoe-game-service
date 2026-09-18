package com.svi.tictactoe_game_service.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.svi.tictactoe_game_service.dto.response.ErrorResponse;
import com.svi.tictactoe_game_service.dto.response.ValidationErrorResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
  public ResponseEntity<ValidationErrorResponse> handleDtoValidation(MethodArgumentNotValidException ex) {
    log.error("error: ", ex);

    // Collects every @Valid failure into a list of strings
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());

    ValidationErrorResponse response = new ValidationErrorResponse("Validation failed", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ValidationErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    log.error("error: ", ex);

    String mainMessage = "Malformed JSON request body.";
    List<String> errors = new ArrayList<>();
    Throwable cause = ex.getCause();

    if (cause instanceof JsonMappingException jsonMappingException) {
      // Extracts the exact field that failed Jackson deserialization
      String fieldPath = jsonMappingException.getPath().stream()
              .map(JsonMappingException.Reference::getFieldName)
              .collect(Collectors.joining("."));

      if (fieldPath.isEmpty()) fieldPath = "unknown field";

      if (cause instanceof InvalidFormatException invalidFormatException) {
        if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isAssignableFrom(UUID.class)) {
          errors.add(fieldPath + ": Invalid UUID format provided.");
        } else {
          errors.add(fieldPath + ": Invalid data format provided.");
        }
      } else {
        errors.add(fieldPath + ": Incorrect data type provided.");
      }
    } else if (cause instanceof JsonParseException) {
      // Catches raw syntax errors like trailing commas or missing quotes
      errors.add("JSON syntax error. Please check for trailing commas or malformed structure.");
    } else {
      errors.add("Request body is missing or unreadable.");
    }

    ValidationErrorResponse response = new ValidationErrorResponse(mainMessage, errors);
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