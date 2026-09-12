package com.svi.tictactoe_game_service.repository;

import com.svi.tictactoe_game_service.entity.Move;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface GameRepository extends CassandraRepository<Move, UUID> {
}
