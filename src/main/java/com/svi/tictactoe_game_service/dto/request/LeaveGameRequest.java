package com.svi.tictactoe_game_service.dto.request;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import jakarta.validation.constraints.NotNull;

public record LeaveGameRequest(
        @NotNull(message = "Player symbol cannot be empty")
        PlayerSymbol playerSymbol
) {}
