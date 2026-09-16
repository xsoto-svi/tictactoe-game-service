package com.svi.tictactoe_game_service.dto.request;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import jakarta.validation.constraints.NotBlank;

public record JoinGameRequest(
        @NotBlank(message = "Room code cannot be empty")
        PlayerSymbol symbol,

        @NotBlank(message = "Player name cannot be empty")
        String name
) {}
