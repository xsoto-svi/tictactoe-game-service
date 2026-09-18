package com.svi.tictactoe_game_service.dto.request.game;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record MoveRequest(
        @NotNull(message = "Room code cannot be null")
        @Pattern(
                regexp = "^[A-Z0-9]{4}$",
                message = "Invalid format for room code."
        )
        String roomCode,

        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Symbol cannot be blank")
        @Pattern(regexp = "[XO]", message = "Symbol must be either 'X' or 'O'")
        String symbol,

        @NotNull(message = "Location cannot be blank")
        @Min(value = 0, message = "Location must be between 0 and 8")
        @Max(value = 8, message = "Location must be between 0 and 8")
        Integer location,

        LocalDateTime dateSave
) {
}
