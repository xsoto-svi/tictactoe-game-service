package com.svi.tictactoe_game_service.util;

import com.svi.tictactoe_game_service.exception.ResourceNotFoundException;

import java.util.Optional;

public final class EntityUtil {

  private EntityUtil() {
  }

  public static <T> T getOrThrow(Optional<T> optional, String notFoundMessage) {
    return optional.orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
  }
}
