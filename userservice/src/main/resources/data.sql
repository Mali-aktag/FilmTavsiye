CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL
);

INSERT INTO app_user (id, username, password, role) VALUES (1, 'admin', '$2a$10$V.uTQutMsFY8ZhTyZJ2HcOH9O5T/9xP6kVpXnlOqc8CDB16Y1RfeG', 'ADMIN');
INSERT INTO app_user (id, username, password, role) VALUES (2, 'user', '$2a$10$M9CvG2JhhGnQy7OWGvFOCuI1TRvPejSXBHWoM7zXqjjSu0nGMwqOu', 'USER');
