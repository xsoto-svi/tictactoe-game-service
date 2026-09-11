package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.constant.PlayerSymbol;
import com.svi.tictactoe_game_service.constant.SuccessMessage;
import com.svi.tictactoe_game_service.model.dto.request.CreateGameRequest;
import com.svi.tictactoe_game_service.model.dto.response.ApiResponse;
import com.svi.tictactoe_game_service.model.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/game")
  public ResponseEntity<ApiResponse> createGame(@Valid CreateGameRequest createGameRequest) {
    PlayerSymbol playerSymbol = gameService.createGame(createGameRequest);

    ApiResponse apiResponse = MatchMakingResponse.builder()
            .message(SuccessMessage.CREATED_GAME.toString())
            .playerSymbol(playerSymbol)
            .build();

    return ResponseEntity.ok(apiResponse);
  }


}
