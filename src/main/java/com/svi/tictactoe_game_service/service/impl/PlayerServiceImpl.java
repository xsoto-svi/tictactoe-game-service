package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.dto.response.player.GetGamesByPlayerResponse;
import com.svi.tictactoe_game_service.dto.response.player.GetPlayersResponse;
import com.svi.tictactoe_game_service.entity.Player;
import com.svi.tictactoe_game_service.repository.PlayerRepository;
import com.svi.tictactoe_game_service.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepository;

  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  public GetPlayersResponse getPlayers(){
    List<Player> players = playerRepository.findAll();

    List<String> playerNames = players.stream()
            .map(Player::getPlayerName)
            .toList();

    return new GetPlayersResponse(playerNames);
  }

  public GetGamesByPlayerResponse getGamesByPlayerName(String name){
    List<Player> players = playerRepository.findAllByPlayerName(name);

    List<UUID> gameIds = players.stream()
            .map(Player::getGameId)
            .toList();

    return new GetGamesByPlayerResponse(gameIds);
  }
}
