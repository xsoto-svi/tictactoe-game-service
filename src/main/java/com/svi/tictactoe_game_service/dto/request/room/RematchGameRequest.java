package com.svi.tictactoe_game_service.dto.request.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record RematchGameRequest(
        @NotNull(message = "Game ID cannot be null")
        @Pattern(
                regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "Invalid UUID format for gameId."
        )
        UUID gameId,

        @NotBlank(message = "Name cannot be empty")
        String name
) {}
