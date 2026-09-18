package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.dto.response.player.GetGamesByPlayerResponse;
import com.svi.tictactoe_game_service.dto.response.player.GetPlayersResponse;
import com.svi.tictactoe_game_service.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/players")
@Validated
public class PlayerController {

  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping
  public ResponseEntity<GetPlayersResponse> getPlayers() {
    GetPlayersResponse response = playerService.getPlayers();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{name}/games")
  public ResponseEntity<GetGamesByPlayerResponse> getGamesByPlayerName(
          @PathVariable String name
  ) {
    GetGamesByPlayerResponse response = playerService.getGamesByPlayerName(name);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
