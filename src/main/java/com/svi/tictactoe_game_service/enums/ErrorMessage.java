package com.svi.tictactoe_game_service.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  ROOM_NOT_FOUND("The requested room code does not exist."),
  INVALID_GAME("Game is inactive or does not exist."),
  DATABASE_ERROR("A database error occurred. Please try again later."),
  TYPE_MISMATCH("Invalid format for parameter %s."),
  UUID_TYPE_MISMATCH("Invalid UUID format for parameter %s."),
  RESOURCE_NOT_FOUND("The requested resource is not found."),
  NAME_ALREADY_TAKEN("Name is already taken.");

  private final String message;

  ErrorMessage(String message) { this.message = message; }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }

}
