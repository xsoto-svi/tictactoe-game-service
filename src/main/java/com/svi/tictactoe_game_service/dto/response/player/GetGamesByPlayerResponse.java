package com.svi.tictactoe_game_service.dto.response.player;

import java.util.List;
import java.util.UUID;

public record GetGamesByPlayerResponse(
        List<UUID> games
) {
}