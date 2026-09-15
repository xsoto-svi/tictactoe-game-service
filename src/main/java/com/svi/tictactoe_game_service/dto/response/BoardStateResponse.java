package com.svi.tictactoe_game_service.dto.response;

import java.util.List;

public record BoardStateResponse(
        List<String> board
) {
}
