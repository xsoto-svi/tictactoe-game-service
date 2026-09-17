package com.svi.tictactoe_game_service.dto.response.room;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;

import java.util.UUID;

public record MatchMakingResponse (
        PlayerSymbol symbol,
        UUID gameId,
        String roomCode
) {
}