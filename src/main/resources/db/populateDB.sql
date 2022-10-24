INSERT INTO users (first_name, last_name, login, password, age)
VALUES ('First Name User 1', 'Last Name User 1', 'user1', 'pass1', 35),
       ('First Name User 2', 'Last Name User 2', 'user2', 'pass2', 37),
       ('First Name User 3', 'Last Name User 3', 'user3', 'pass3', 38),
       ('First Name User 4', 'Last Name User 4', 'user4', 'pass4', 54);

INSERT INTO roles(id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 2),
       (4, 1);