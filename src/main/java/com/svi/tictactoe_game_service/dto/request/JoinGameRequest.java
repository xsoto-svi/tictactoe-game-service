package com.svi.tictactoe_game_service.dto.request;

import com.svi.tictactoe_game_service.constant.PlayerSymbol;

public record JoinGameRequest(
        String roomCode,
        PlayerSymbol symbol
) {}
