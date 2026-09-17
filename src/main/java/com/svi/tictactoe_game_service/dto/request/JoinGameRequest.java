package com.svi.tictactoe_game_service.dto.request;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import jakarta.validation.constraints.NotBlank;

public record JoinGameRequest(
        @NotBlank(message = "Symbol cannot be empty")
        PlayerSymbol symbol,

        @NotBlank(message = "Name cannot be empty")
        String name
) {}
