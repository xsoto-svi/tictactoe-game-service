package com.svi.tictactoe_game_service.service.impl;

import com.svi.tictactoe_game_service.dto.request.game.MoveRequest;
import com.svi.tictactoe_game_service.dto.response.game.GetGamesByGameIdResponse;
import com.svi.tictactoe_game_service.dto.response.game.MoveResponse;
import com.svi.tictactoe_game_service.entity.Move;
import com.svi.tictactoe_game_service.entity.Room;
import com.svi.tictactoe_game_service.enums.GameStatus;
import com.svi.tictactoe_game_service.enums.MoveError;
import com.svi.tictactoe_game_service.enums.PlayerSymbol;
import com.svi.tictactoe_game_service.exception.InvalidGameException;
import com.svi.tictactoe_game_service.exception.InvalidMoveException;
import com.svi.tictactoe_game_service.mapper.MoveMapper;
import com.svi.tictactoe_game_service.repository.MoveRepository;
import com.svi.tictactoe_game_service.repository.RoomRepository;
import com.svi.tictactoe_game_service.service.GameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

  private static final int[][] WINNING_PATTERNS = {
          {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
          {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
          {0, 4, 8}, {2, 4, 6}             // Diagonals
  };

  private final RoomRepository roomRepository;
  private final MoveRepository moveRepository;

  public GameServiceImpl(RoomRepository roomRepository, MoveRepository moveRepository) {
    this.roomRepository = roomRepository;
    this.moveRepository = moveRepository;
  }

  @Override
  public MoveResponse processMove(UUID gameId, MoveRequest request) {
    Room room = roomRepository.findByRoomCodeAndGameId(request.roomCode(), gameId);
    if (room == null) {
      throw new InvalidGameException();
    }

    List<Move> existingMoves = moveRepository.findAllByGameId(gameId);
    int nextMoveNumber = existingMoves.size() + 1;

    Move currentMove = MoveMapper.toMoveEntity(request, gameId, nextMoveNumber);

    validateMove(currentMove, existingMoves);

    moveRepository.save(currentMove);

    // Add the new move to the local list to evaluate the final board state
    existingMoves.add(currentMove);

    boolean hasWon = hasAnyPlayerWon(existingMoves);
    boolean isDraw = existingMoves.size() == 9 && !hasWon;

    GameStatus gameStatus = GameStatus.IN_PROGRESS;
    PlayerSymbol nextTurn = null;
    PlayerSymbol winner = null;

    if (hasWon || isDraw) {
      gameStatus = GameStatus.GAME_OVER;

      if (hasWon) {
        winner = PlayerSymbol.valueOf(currentMove.getSymbol().toUpperCase());
      }

      room.setStatus(gameStatus);
      roomRepository.save(room);

    } else {
      // Game continues, alternate the turn
      nextTurn = "X".equalsIgnoreCase(currentMove.getSymbol()) ? PlayerSymbol.O : PlayerSymbol.X;
    }

    return new MoveResponse(hasWon, isDraw, gameStatus, nextTurn, winner);
  }

  @Override
  public GetGamesByGameIdResponse getMovesByGameId(UUID gameId) {
    List<Move> moves = moveRepository.findAllByGameId(gameId);

    List<GetGamesByGameIdResponse.MoveDto> moveDtoList = moves.
            stream()
            .map(MoveMapper::toMoveDto)
            .toList();

    return new GetGamesByGameIdResponse(moveDtoList);
  }

  // UTILS
  private void validateMove(Move currentMove, List<Move> existingMoves) {
    if (existingMoves.size() >= 9) {
      throw new InvalidMoveException(MoveError.BOARD_FULL);
    }

    if (hasAnyPlayerWon(existingMoves)) {
      throw new InvalidMoveException(MoveError.GAME_ALREADY_FINISHED);
    }

    boolean cellOccupied = existingMoves.stream()
            .anyMatch(m -> m.getLocation() == currentMove.getLocation());
    if (cellOccupied) {
      throw new InvalidMoveException(MoveError.LOCATION_OCCUPIED, currentMove.getLocation());
    }

    if (existingMoves.isEmpty()) {
      if (!"X".equalsIgnoreCase(currentMove.getSymbol())) {
        throw new InvalidMoveException(MoveError.X_MUST_START);
      }
      return;
    }

    Move lastMove = existingMoves.getLast();
    boolean isAlternatingSymbol = !lastMove.getSymbol().equalsIgnoreCase(currentMove.getSymbol());
    boolean isDifferentPlayer = !lastMove.getName().equalsIgnoreCase(currentMove.getName());

    if (!isAlternatingSymbol || !isDifferentPlayer) {
      throw new InvalidMoveException(MoveError.NOT_YOUR_TURN);
    }
  }

  private boolean hasAnyPlayerWon(List<Move> moves) {
    Set<Integer> xPositions = moves.stream()
            .filter(m -> "X".equalsIgnoreCase(m.getSymbol()))
            .map(Move::getLocation)
            .collect(Collectors.toSet());

    Set<Integer> oPositions = moves.stream()
            .filter(m -> "O".equalsIgnoreCase(m.getSymbol()))
            .map(Move::getLocation)
            .collect(Collectors.toSet());

    return isWinner(xPositions) || isWinner(oPositions);
  }

  private boolean isWinner(Set<Integer> positions) {
    for (int[] pattern : WINNING_PATTERNS) {
      if (positions.contains(pattern[0]) &&
              positions.contains(pattern[1]) &&
              positions.contains(pattern[2])) {
        return true;
      }
    }
    return false;
  }
}
