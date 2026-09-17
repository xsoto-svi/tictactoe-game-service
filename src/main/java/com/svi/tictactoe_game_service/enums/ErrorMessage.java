package com.svi.tictactoe_game_service.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  ROOM_NOT_FOUND("The requested room code does not exist."),
  DATABASE_ERROR("A database error occurred. Please try again later."),
  METHOD_ARGUMENT_MISMATCH("Failed to convert parameter '%s' with value '%s' to required type '%s'"),
  RESOURCE_NOT_FOUND("The requested resource is not found.");

  private final String message;

  ErrorMessage(String message) { this.message = message; }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }

}
