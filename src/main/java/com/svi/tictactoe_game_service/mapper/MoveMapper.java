package com.svi.tictactoe_game_service.mapper;

import com.svi.tictactoe_game_service.dto.request.game.MoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGamesByGameIdResponse;
import com.svi.tictactoe_game_service.entity.Move;

import java.time.LocalDateTime;
import java.util.UUID;

public class MoveMapper {
  public static GetGamesByGameIdResponse.MoveDto toMoveDto(Move move) {
    return new GetGamesByGameIdResponse.MoveDto(
            move.getName(),
            move.getSymbol(),
            move.getLocation()
    );
  }

  public static Move toMoveEntity(MoveRequest request, UUID gameId, int moveNumber) {
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
