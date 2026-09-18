package com.svi.tictactoe_game_service.dto.request.room;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record LeaveGameRequest(
        @NotNull(message = "Game ID cannot be null")
        @Pattern(
                regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "Invalid UUID format for gameId."
        )
        UUID gameId,

        @NotNull(message = "Symbol cannot be empty")
        PlayerSymbol symbol
) {}
