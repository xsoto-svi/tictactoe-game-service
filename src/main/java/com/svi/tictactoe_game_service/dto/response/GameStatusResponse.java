package com.svi.tictactoe_game_service.dto.response;

import com.svi.tictactoe_game_service.enums.GameStatus;

public record GameStatusResponse(
        GameStatus gameStatus
) {}
