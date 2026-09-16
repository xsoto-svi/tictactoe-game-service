package com.svi.tictactoe_game_service.service;

import com.svi.tictactoe_game_service.dto.request.CreateGameRequest;
import com.svi.tictactoe_game_service.dto.request.JoinGameRequest;
import com.svi.tictactoe_game_service.dto.request.LeaveGameRequest;
import com.svi.tictactoe_game_service.dto.response.MatchMakingResponse;
import com.svi.tictactoe_game_service.dto.response.game.GameStatusResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetGamesByRoomResponse;
import com.svi.tictactoe_game_service.dto.response.room.GetRoomsResponse;

public interface RoomService {
  MatchMakingResponse createGame(String roomCode, CreateGameRequest request);
  MatchMakingResponse joinGame(String roomCode, JoinGameRequest request);
  GameStatusResponse checkGameStatus(String roomCode);
  void leaveGame(String roomCode, LeaveGameRequest request);
  void rematchGame(String roomCode, JoinGameRequest request);
  GetRoomsResponse getRooms();
  GetGamesByRoomResponse getGamesByRoomCode(String roomCode);
}
