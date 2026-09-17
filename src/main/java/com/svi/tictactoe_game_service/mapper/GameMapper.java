package com.svi.tictactoe_game_service.mapper;

import com.svi.tictactoe_game_service.dto.request.room.SaveMoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGameResponse;
import com.svi.tictactoe_game_service.entity.Move;

import java.time.LocalDateTime;
import java.util.UUID;

public class GameMapper {
  public static GetGameResponse.MoveDto toMoveDto(Move move) {
    return new GetGameResponse.MoveDto(
            move.getName(),
            move.getSymbol(),
            move.getLocation()
    );
  }

  public static Move toMoveEntity(SaveMoveRequest request, UUID gameId, int moveNumber) {
    Move move = new Move();
    move.setGameId(gameId);
    move.setMoveNumber(moveNumber);
    move.setName(request.name());
    move.setSymbol(request.symbol());
    move.setLocation(request.location());
    move.setDateSaved(request.dateSave() != null ? request.dateSave() : LocalDateTime.now());
    return move;
  }
}
