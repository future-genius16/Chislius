CREATE TABLE users
(
    id         BIGINT PRIMARY KEY auto_increment UNIQUE,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    token      VARCHAR(255) UNIQUE,
    rating     INT
);