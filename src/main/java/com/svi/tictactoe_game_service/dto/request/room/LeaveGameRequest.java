package com.svi.tictactoe_game_service.dto.request.room;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LeaveGameRequest(
        @NotNull(message = "Game ID cannot be null")
        UUID gameId,

        @NotNull(message = "Symbol cannot be empty")
        PlayerSymbol symbol
) {}
