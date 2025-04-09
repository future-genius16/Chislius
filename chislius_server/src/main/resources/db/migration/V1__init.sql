CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255)       NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rating        INT
);

CREATE TABLE rooms
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id   BIGINT,
    type       VARCHAR(10) NOT NULL,
    status     VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    ended_at   TIMESTAMP,
    join_link  VARCHAR(255) UNIQUE,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE players
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT,
    user_id BIGINT,
    score   INT DEFAULT 0,
    FOREIGN KEY (room_id) REFERENCES rooms (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);