package com.svi.tictactoe_game_service.dto.response.room;

import java.util.List;

public record GetRoomsResponse(
        List<String> rooms
) {
}