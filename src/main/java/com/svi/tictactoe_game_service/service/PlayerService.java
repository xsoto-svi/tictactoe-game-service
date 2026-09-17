package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.response.player.GetGamesByPlayerResponse;
import com.svi.tictactoe_game_service.dto.response.player.GetPlayersResponse;

public interface PlayerService {
  GetPlayersResponse getPlayers();
  GetGamesByPlayerResponse getGamesByPlayerName(String name);
}
