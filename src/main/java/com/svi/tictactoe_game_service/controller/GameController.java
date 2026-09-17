package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.dto.request.room.SaveMoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGamesByGameIdResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;
import com.svi.tictactoe_game_service.service.GameService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
  public ResponseEntity<Void> addMove(
          @PathVariable
          UUID gameId,
          @Valid @RequestBody SaveMoveRequest request
  ) {
    gameService.addMove(gameId, request);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/{gameId}/games")
  public ResponseEntity<GetGamesByGameIdResponse> getGamesByRoomCode(
          @PathVariable
          UUID gameId
  ) {
    GetGamesByGameIdResponse response = gameService.getMovesByGameId(gameId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
