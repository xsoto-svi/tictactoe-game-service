package com.svi.tictactoe_game_service.dto.request.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RematchGameRequest(
        @NotNull(message = "Game ID cannot be null")
        UUID gameId,

        @NotBlank(message = "Name cannot be empty")
        String name
) {}
