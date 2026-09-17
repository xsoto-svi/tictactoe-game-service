package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.dto.request.room.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("rooms")
  public ResponseEntity<MatchMakingResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
    MatchMakingResponse response = roomService.createGame(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("rooms/{roomCode}/join")
  public ResponseEntity<MatchMakingResponse> joinGame(
          @PathVariable String roomCode,
          @Valid @RequestBody JoinGameRequest request) {
    MatchMakingResponse response = roomService.joinGame(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("rooms/{roomCode}/leave")
  public ResponseEntity<MatchMakingResponse> leaveGame(
          @PathVariable String roomCode,
          @Valid @RequestBody LeaveGameRequest request) {
    MatchMakingResponse response = roomService.leaveGame(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}