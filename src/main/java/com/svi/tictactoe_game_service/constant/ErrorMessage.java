package com.svi.tictactoe_game_service.constant;

import com.svi.tictactoe_game_service.exception.ResourceNotFoundException;
import lombok.Getter;

@Getter
public enum ErrorMessage {
  ROOM_NOT_FOUND("The requested room code does not exist."),
  RESOURCE_NOT_FOUND("The requested resource is not found.");

  private final String message;

  ErrorMessage(String message) { this.message = message; }
}
