package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.room.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.request.room.RematchGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.dto.response.game.GameStatusResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;
import com.svi.tictactoe_game_service.dto.response.room.RematchResponse;

public interface RoomService {
  MatchMakingResponse createGame(String roomCode, CreateGameRequest request);
  MatchMakingResponse joinGame(String roomCode, JoinGameRequest request);
  GameStatusResponse checkGameStatus(String roomCode);
  void leaveGame(String roomCode, LeaveGameRequest request);
  RematchResponse rematchGame(String roomCode, RematchGameRequest request);
  GetRoomsResponse getRooms();
  GetGamesByRoomResponse getGamesByRoomCode(String roomCode);
}
