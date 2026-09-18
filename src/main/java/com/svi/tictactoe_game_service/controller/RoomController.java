package com.svi.tictactoe_game_service.controller;

import com.svi.tictactoe_game_service.dto.request.room.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.response.room.MatchMakingResponse;
import com.svi.tictactoe_game_service.dto.response.game.GameStatusResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;
import com.svi.tictactoe_game_service.dto.response.room.RematchResponse;
import com.svi.tictactoe_game_service.service.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@Validated
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping
  public ResponseEntity<MatchMakingResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
    MatchMakingResponse response = roomService.createGame(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/{roomCode}/join")
  public ResponseEntity<MatchMakingResponse> joinGame(
          @PathVariable
          @Pattern(regexp = "^[A-Z0-9]{4}$", message = "Room code must be exactly 4 uppercase alphanumeric characters")
          String roomCode,

          @Valid @RequestBody JoinGameRequest request
  ) {
    MatchMakingResponse response = roomService.joinGame(roomCode, request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/{roomCode}/leave")
  public ResponseEntity<Void> leaveGame(
          @PathVariable
          @Pattern(regexp = "^[A-Z0-9]{4}$", message = "Room code must be exactly 4 uppercase alphanumeric characters")
          String roomCode,

          @Valid @RequestBody LeaveGameRequest request
  ) {
    roomService.leaveGame(roomCode, request);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/{roomCode}/rematch")
  public ResponseEntity<RematchResponse> rematchGame(
          @PathVariable
          @Pattern(regexp = "^[A-Z0-9]{4}$", message = "Room code must be exactly 4 uppercase alphanumeric characters")
          String roomCode
  ) {
    RematchResponse response = roomService.rematchGame(roomCode);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{roomCode}/status")
  public ResponseEntity<GameStatusResponse> checkRoomStatus(
          @PathVariable
          @Pattern(regexp = "^[A-Z0-9]{4}$", message = "Room code must be exactly 4 uppercase alphanumeric characters")
          String roomCode
  ) {
    GameStatusResponse response = roomService.checkRoomStatus(roomCode);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping
  public ResponseEntity<GetRoomsResponse> getRooms() {
    GetRoomsResponse response = roomService.getRooms();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{roomCode}/games")
  public ResponseEntity<GetGamesByRoomResponse> getGamesByRoomCode(
          @PathVariable
          @Pattern(regexp = "^[A-Z0-9]{4}$", message = "Room code must be exactly 4 uppercase alphanumeric characters")
          String roomCode
  ) {
    GetGamesByRoomResponse response = roomService.getGamesByRoomCode(roomCode);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}