package com.svi.tictactoe_game_service.dto.response.game;

import com.svi.tictactoe_game_service.enums.GameStatus;
import com.svi.tictactoe_game_service.enums.PlayerSymbol;

public record MoveResponse(
        boolean hasWon,
        boolean isDraw,
        GameStatus gameStatus,
        PlayerSymbol nextTurn,
        PlayerSymbol winner
) {
}