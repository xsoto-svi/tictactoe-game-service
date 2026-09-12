package com.svi.tictactoe_game_service.entity;

import com.svi.tictactoe_game_service.constant.GameStatus;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table("rooms")
public class Room {

  @PrimaryKeyColumn(name = "room_code", type = PrimaryKeyType.PARTITIONED)
  private String roomCode;

  @PrimaryKeyColumn(name = "game_id", type = PrimaryKeyType.CLUSTERED)
  private UUID gameId;

  @Column("status")
  private GameStatus status;
}
