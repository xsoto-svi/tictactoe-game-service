package com.svi.tictactoe_game_service.dto.response;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;

public record MatchMakingResponse (
        PlayerSymbol symbol
) {
}