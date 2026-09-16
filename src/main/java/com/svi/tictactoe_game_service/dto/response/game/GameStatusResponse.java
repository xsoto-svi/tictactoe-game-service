package com.svi.tictactoe_game_service.dto.response.game;

import com.svi.tictactoe_game_service.enums.GameStatus;

public record GameStatusResponse(
        GameStatus gameStatus
) {}
