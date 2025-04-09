INSERT INTO users (username, password_hash, rating)
VALUES ('User1', 'q384n0vtq39jtq3t', 50);
INSERT INTO users (username, password_hash, rating)
VALUES ('User2', 'nu8vtn7ytburybvi', 100);
INSERT INTO rooms (owner_id, type, status, join_link)
VALUES (1, 'RRR', 'AAA', 'vrrnv92q9i9i');
INSERT INTO players (room_id, user_id, score)
VALUES (1, 1, 5);
INSERT INTO players (room_id, user_id, score)
VALUES (1, 2, 7);