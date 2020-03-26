DELETE FROM USER;
DELETE FROM RESTAURANT;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO USER (ROLE, NAME, EMAIL, PASSWORD)
VALUES ('REGULAR_USER', 'User 2', 'abc@yandex.ru', 'password1'),    --id: 1000000
       ('REGULAR_USER', 'User 1', 'def@yandex.ru', 'password2'),    --id: 1000001
       ('ADMIN', 'Admin 2', 'asd@yandex.ru', 'admin2'),
       ('ADMIN', 'Admin 1', 'qwe@yandex.ru', 'admin1'),
       ('REGULAR_USER', 'User 3', 'ghi@yandex.ru', 'password3');    --id: 1000004

INSERT INTO RESTAURANT (NAME)
VALUES ('Корейский'),       --id: 100005
       ('Японский'),        --id: 100006
       ('Грузинский');      --id: 100007

INSERT INTO MENU (DATE, RESTAURANT_ID)
VALUES ('2020-03-24', '100005'),
       ('2020-03-24', '100006'),
       ('2020-03-25', '100005'),
       ('2020-03-25', '100006'),
       ('2020-03-25', '100007');

INSERT INTO DISH (NAME, PRICE, MENU_ID)
VALUES ('Корейское блюдо 1', 20000, 100008),
       ('Корейское блюдо 3', 15000, 100008),
       ('Корейское блюдо 2', 35000, 100008),
       ('Японское блюдо 1', 35000, 100009),
       ('Корейское блюдо 1', 20000, 100010),
       ('Корейское блюдо 5', 30000, 100010),
       ('Корейское блюдо 4', 40000, 100010),
       ('Японское блюдо 2', 75000, 100011),
       ('Японское блюдо 3', 55000, 100011),
       ('Японское блюдо 5', 42000, 100011),
       ('Японское блюдо 4', 30000, 100011),
       ('Грузинское блюдо 2', 13000, 100012),
       ('Грузинское блюдо 1', 30000, 100012);

INSERT INTO VOTE (DATE, RESTAURANT_ID, USER_ID)
VALUES ('2020-03-24', 100005, 100001),
       ('2020-03-24', 100006, 100004),
       ('2020-03-24', 100006, 100000),
       ('2020-03-25', 100005, 100000),
       ('2020-03-25', 100006, 100004),
       ('2020-03-25', 100005, 100001);