package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.room.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.RematchGameRequest;
import com.svi.tictactoe_game_service.dto.response.room.MatchMakingResponse;
import com.svi.tictactoe_game_service.dto.response.game.GameStatusResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;
import com.svi.tictactoe_game_service.dto.response.room.RematchResponse;

import java.util.UUID;

public interface RoomService {
  MatchMakingResponse createGame(CreateGameRequest request);
  MatchMakingResponse joinGame(String roomCode, JoinGameRequest request);
  void leaveGame(String roomCode, UUID gameId);
  RematchResponse rematchGame(String roomCode, UUID gameId, RematchGameRequest request);
  GameStatusResponse checkRoomStatus(String roomCode);
  GetRoomsResponse getRooms();
  GetGamesByRoomResponse getGamesByRoomCode(String roomCode);
}
