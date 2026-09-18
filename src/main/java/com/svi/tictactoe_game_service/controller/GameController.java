package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.dto.request.game.MoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGamesByGameIdResponse;
import com.svi.tictactoe_game_service.dto.response.game.MoveResponse;
import com.svi.tictactoe_game_service.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
@Validated
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/{gameId}/moves")
  public ResponseEntity<MoveResponse> addMove(
          @PathVariable
          UUID gameId,
          @Valid @RequestBody MoveRequest request
  ) {
    MoveResponse response = gameService.processMove(gameId, request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{gameId}/games")
  public ResponseEntity<GetGamesByGameIdResponse> getGamesByGameId(
          @PathVariable
          UUID gameId
  ) {
    GetGamesByGameIdResponse response = gameService.getMovesByGameId(gameId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
