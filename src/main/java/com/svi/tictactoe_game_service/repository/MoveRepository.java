package com.svi.tictactoe_game_service.repository;

import com.svi.tictactoe_game_service.entity.Move;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MoveRepository extends CassandraRepository<Move, UUID> {
  List<Move> findAllByGameId(UUID gameId);
}
