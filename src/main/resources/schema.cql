CREATE TABLE batch1_2026_trainees.rooms (room_code text,game_id uuid,status text,PRIMARY KEY (room_code, game_id));

CREATE TABLE batch1_2026_trainees.players (name text,game_id uuid,room_code text,PRIMARY KEY (name, game_id));

CREATE TABLE batch1_2026_trainees.moves (game_id uuid,name text,symbol text,location int,date_saved timestamp,PRIMARY KEY (game_id, date_saved)) WITH CLUSTERING ORDER BY (date_saved ASC);