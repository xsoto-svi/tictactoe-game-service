package com.svi.tictactoe_game_service.dto.response.room;

import java.util.List;
import java.util.UUID;

public record GetGamesByRoomResponse(
        List<UUID> games
) {
}
