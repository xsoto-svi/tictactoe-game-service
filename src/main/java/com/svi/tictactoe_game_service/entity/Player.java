package com.svi.tictactoe_game_service.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table("players")
public class Player {

  @PrimaryKeyColumn(name = "room_code", type = PrimaryKeyType.PARTITIONED)
  private String playerName;

  @PrimaryKeyColumn(name = "game_id", type = PrimaryKeyType.CLUSTERED)
  private UUID gameId;

  @Column("room_code")
  private String roomCode;
}
