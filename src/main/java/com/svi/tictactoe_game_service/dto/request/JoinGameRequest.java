package com.svi.tictactoe_game_service.dto.request;

import com.svi.tictactoe_game_service.enums.PlayerSymbol;

public record JoinGameRequest(
        String roomCode,
        PlayerSymbol symbol
) {}
