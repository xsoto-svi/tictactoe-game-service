package com.svi.tictactoe_game_service.dto.response.player;

import java.util.List;

public record GetPlayersResponse(
        List<String> players
) {}
