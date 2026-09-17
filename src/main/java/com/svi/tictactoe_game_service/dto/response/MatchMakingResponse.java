package com.svi.tictactoe_game_service.dto.response;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;

import java.util.UUID;

public record MatchMakingResponse (
        PlayerSymbol symbol,
        UUID gameId,
        String roomCode
) {
}