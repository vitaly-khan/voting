-- noinspection SqlWithoutWhereForFile
DELETE
FROM USER;
DELETE
FROM RESTAURANT;
-- noinspection SqlResolve
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO USER (ROLE, NAME, EMAIL, PASSWORD)
VALUES ('USER', 'User 2', 'user2@gmail.com', '{noop}password2'), --id: 1000000
       ('USER', 'User 1', 'user1@gmail.com', '{noop}password1'), --id: 1000001
       ('ADMIN', 'Admin 2', 'admin2@gmail.com', '{noop}admin2'),
       ('ADMIN', 'Admin 1', 'admin1@gmail.com', '{noop}admin1'),
       ('USER', 'User 3', 'user3@gmail.com', '{noop}password3'); --id: 1000004

INSERT INTO RESTAURANT (NAME)
VALUES ('Korean'),   --id: 100005
       ('Japanese'), --id: 100006
       ('Georgian'); --id: 100007
INSERT INTO RESTAURANT(NAME, ENABLED)
VALUES ('Disabled', false); --id: 100008

INSERT INTO MENU (DATE, RESTAURANT_ID)
VALUES ('2020-05-03', '100005'), --id: 100009
       ('2020-05-03', '100006'), --id: 100010
       ('2020-05-03', '100007'), --id: 100011
       (now(), '100005'),        --id: 100012
       (now(), '100006');        --id: 100013

INSERT INTO DISH (NAME, PRICE, MENU_ID)
VALUES ('Korean dish 1', 20000, 100009),
       ('Korean dish 3', 15000, 100009),
       ('Korean dish 2', 35000, 100009),
       ('Japanese dish 1', 35000, 100010),
       ('Georgian dish 2', 13000, 100011),
       ('Georgian dish 1', 30000, 100011),
       ('Korean dish 1', 20000, 100012),
       ('Korean dish 5', 30000, 100012),
       ('Korean dish 4', 40000, 100012),
       ('Japanese dish 2', 75000, 100013),
       ('Japanese dish 3', 55000, 100013),
       ('Japanese dish 5', 42000, 100013),
       ('Japanese dish 4', 30000, 100013);

INSERT INTO VOTE (DATE, MENU_ID, USER_ID)
VALUES ('2020-05-03', 100009, 100001), --id: 100026
       ('2020-05-03', 100010, 100004),
       ('2020-05-03', 100010, 100000),
       (now(), 100011, 100000),
       (now(), 100012, 100004);