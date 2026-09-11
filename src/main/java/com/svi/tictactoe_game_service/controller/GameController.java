package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.constant.GameStatus;
import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import com.svi.tictactoe_game_service.constant.SuccessMessage;
import com.svi.tictactoe_game_service.model.dto.request.CreateGameRequest;
import com.svi.tictactoe_game_service.model.dto.response.ApiResponse;
import com.svi.tictactoe_game_service.model.dto.response.GameStatusResponse;
import com.svi.tictactoe_game_service.model.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("create")
  public ResponseEntity<ApiResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
    PlayerSymbol playerSymbol = gameService.createGame(request);

    ApiResponse apiResponse = MatchMakingResponse.builder()
            .message(SuccessMessage.CREATED_GAME.toString())
            .playerSymbol(playerSymbol)
            .build();

    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @PostMapping("join")
  public ResponseEntity<ApiResponse> joinGame(@Valid @RequestBody CreateGameRequest request) {
    PlayerSymbol playerSymbol = gameService.joinGame(request);

    ApiResponse apiResponse = MatchMakingResponse.builder()
            .message(SuccessMessage.JOIN_GAME.toString())
            .playerSymbol(playerSymbol)
            .build();

    return ResponseEntity.ok(apiResponse);
  }

  @PostMapping("status")
  public ResponseEntity<ApiResponse> checkGameStatus() {
    GameStatus gameStatus = gameService.checkGameStatus();

    ApiResponse apiResponse = GameStatusResponse.builder()
            .message(SuccessMessage.JOIN_GAME.toString())
            .gameStatus(gameStatus)
            .build();

    return ResponseEntity.ok(apiResponse);
  }

  @PostMapping("board-state")
  public ResponseEntity<ApiResponse> checkBoardState() {
    List<String> board = gameService.checkBoardState();
  }
}
