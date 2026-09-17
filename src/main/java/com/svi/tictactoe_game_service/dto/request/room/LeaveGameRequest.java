package com.svi.tictactoe_game_service.dto.request.room;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import jakarta.validation.constraints.NotNull;

public record LeaveGameRequest(
        @NotNull(message = "Symbol cannot be empty")
        PlayerSymbol symbol
) {}
