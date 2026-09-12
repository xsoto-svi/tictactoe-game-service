package com.svi.tictactoe_game_service.repository;

import com.svi.tictactoe_game_service.entity.Room;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CassandraRepository<Room, String> {
}
