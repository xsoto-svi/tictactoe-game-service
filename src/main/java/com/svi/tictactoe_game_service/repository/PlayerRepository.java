package com.svi.tictactoe_game_service.repository;

import com.svi.tictactoe_game_service.entity.Player;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerRepository extends CassandraRepository<Player, String> {
  boolean existsByGameIdAndName(UUID gameId, String name);
  List<Player> findAllByGameId(UUID gameId);
  List<Player> findAllByName(String name);
}
