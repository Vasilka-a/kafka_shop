CREATE TABLE IF NOT EXISTS users
(
    user_id    BIGSERIAL PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name  VARCHAR NOT NULL,
    user_email VARCHAR NOT NULL UNIQUE
);

INSERT INTO users (user_id, first_name, last_name, user_email)
VALUES (1, 'Anna', 'Ivanova', 'user@user.com'),
       (2, 'Ivan', 'Kotov', 'user2@user.com'),
       (3, 'Olga', 'Smirnova', 'user3@user.com');
