package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.constant.GameStatus;
import com.svi.tictactoe_game_service.dto.request.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.response.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("create")
  public ResponseEntity<MatchMakingResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
    MatchMakingResponse response = gameService.createGame(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("join")
  public ResponseEntity<MatchMakingResponse> joinGame(@Valid @RequestBody JoinGameRequest request) {
    MatchMakingResponse response = gameService.joinGame(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("status")
  public ResponseEntity<GameStatus> checkGameStatus() {
    GameStatus gameStatus = gameService.checkGameStatus();
    return ResponseEntity.status(HttpStatus.OK).body(gameStatus);
  }
}
