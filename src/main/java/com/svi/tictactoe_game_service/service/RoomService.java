package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;

public interface RoomService {
  MatchMakingResponse createGame(String roomCode, CreateGameRequest request);
  MatchMakingResponse joinGame(String roomCode, JoinGameRequest request);
  void leaveGame(String roomCode);
  void rematchGame(String roomCode, JoinGameRequest request);
  GetRoomsResponse getRooms();
  GetGamesByRoomResponse getGamesByRoomCode(String roomCode);
}
