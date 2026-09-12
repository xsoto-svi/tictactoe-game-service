package com.svi.tictactoe_game_service.entity;

import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("moves")
public class Move {

  @PrimaryKeyColumn(name = "game_id", type = PrimaryKeyType.PARTITIONED)
  private UUID gameId;

  @PrimaryKeyColumn(name = "move_number", type = PrimaryKeyType.CLUSTERED)
  private int moveNumber;

  @Column("player_name")
  private String playerName;

  @Column("symbol")
  private String symbol;

  @Column("location")
  private int location;

  @Column("created_at")
  private LocalDateTime createdAt;
}