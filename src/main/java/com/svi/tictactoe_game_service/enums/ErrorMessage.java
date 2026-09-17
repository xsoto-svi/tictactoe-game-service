package com.svi.tictactoe_game_service.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  ROOM_NOT_FOUND("The requested room code does not exist."),
  LOCATION_OCCUPIED("Location %s is already occupied"),
  RESOURCE_NOT_FOUND("The requested resource is not found.");

  private final String message;

  ErrorMessage(String message) { this.message = message; }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }

}
