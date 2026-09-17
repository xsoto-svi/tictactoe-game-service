package com.svi.tictactoe_game_service.dto.request.room;

import jakarta.validation.constraints.NotBlank;

public record JoinGameRequest(
        @NotBlank(message = "Name cannot be empty")
        String name
) {}
