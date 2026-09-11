package com.svi.tictactoe_game_service.constant;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  ROOM_NOT_FOUND("The requested room code does not exist.");

  private final String message;

  ErrorMessage(String message) { this.message = message; }
}
