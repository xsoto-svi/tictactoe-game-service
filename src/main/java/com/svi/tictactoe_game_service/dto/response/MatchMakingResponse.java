package com.svi.tictactoe_game_service.dto.response;

import com.svi.tictactoe_game_service.constant.PlayerSymbol;

public record MatchMakingResponse (
        PlayerSymbol symbol
) {
}