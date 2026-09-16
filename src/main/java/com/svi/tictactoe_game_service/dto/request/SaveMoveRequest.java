package com.svi.tictactoe_game_service.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record SaveMoveRequest(
        @NotNull(message = "Game ID cannot be null")
        @Pattern(
                regexp = "^[A-Z0-9]{4}R*_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "Invalid UUID format for gameId."
        )
        String gameId,

        @NotBlank(message = "Player ID cannot be blank")
        String playerName,

        @NotBlank(message = "Symbol cannot be blank")
        @Pattern(regexp = "[XO]", message = "Symbol must be either 'X' or 'O'")
        String symbol,

        @NotNull(message = "Location cannot be blank")
        @Min(value = 0, message = "Location must be between 0 and 8")
        @Max(value = 8, message = "Location must be between 0 and 8")
        int location,

        LocalDateTime dateSave
) {
}
