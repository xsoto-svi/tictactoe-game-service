package com.svi.tictactoe_game_service.dto.response;

import java.util.List;

public record ValidationErrorResponse(
        String message,
        List<String> errors
) {
}
