DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (description, date_time, calories, user_id)
VALUES ('Breakfast', '2020-01-30 10:00:00', 500, 100000),
       ('Lunch', '2020-01-30 13:00:00', 1000, 100000),
       ('Supper', '2020-01-30 20:00:00', 500, 100000),
       ('Edge meal value', '2020-01-31 00:00:00', 100, 100000),
       ('Breakfast', '2020-01-31 10:00:00', 1000, 100000),
       ('Lunch', '2020-01-31 13:00:00', 500, 100000),
       ('Supper', '2020-01-31 20:00:00', 410, 100000),
       ('Admin Breakfast', '2020-01-30 10:00:00', 100, 100001),
       ('Admin Lunch', '2020-01-30 13:00:00', 2000, 100001),
       ('Admin Supper', '2020-01-30 20:00:00', 500, 100001);

