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
       ('Georgian'), --id: 100007
       ('Italian'), --id: 100008
       ('French'); --id: 100009
INSERT INTO RESTAURANT(NAME, ENABLED)
VALUES ('Thai', false); --id: 100010

INSERT INTO MENU (DATE, RESTAURANT_ID)
VALUES ('2020-05-03', '100005'), --id: 100011
       ('2020-05-03', '100006'), --id: 100012
       ('2020-05-03', '100007'), --id: 100013
       (now(), '100005'),        --id: 100014
       (now(), '100006'),        --id: 100015
       (now(), '100008');        --id: 100016
INSERT INTO MENU (DATE, RESTAURANT_ID, ENABLED)
VALUES (now(), '100009', FALSE); --id: 100017

INSERT INTO DISH (NAME, PRICE, MENU_ID)
VALUES ('Korean dish 1', 20000, 100011),
       ('Korean dish 3', 15000, 100011),
       ('Korean dish 2', 35000, 100011),
       ('Japanese dish 1', 35000, 100012),
       ('Georgian dish 2', 13000, 100013),
       ('Georgian dish 1', 30000, 100013),
       ('Korean dish 1', 20000, 100014),
       ('Korean dish 5', 30000, 100014),
       ('Korean dish 4', 40000, 100014),
       ('Japanese dish 2', 75000, 100015),
       ('Japanese dish 3', 55000, 100015),
       ('Japanese dish 5', 42000, 100015),
       ('Japanese dish 4', 30000, 100015);
INSERT INTO DISH (NAME, PRICE, MENU_ID, ENABLED)
VALUES ('Italian dish 1', 30000, 100017, FALSE);

INSERT INTO VOTE (DATE, MENU_ID, USER_ID)
VALUES ('2020-05-03', 100011, 100001), --id: 100032
       ('2020-05-03', 100012, 100004),
       ('2020-05-03', 100012, 100000),
       (now(), 100014, 100000),
       (now(), 100014, 100004);