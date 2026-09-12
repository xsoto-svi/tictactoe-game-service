package com.svi.tictactoe_game_service.repository;

import com.svi.tictactoe_game_service.entity.Player;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CassandraRepository<Player, String> {
}
