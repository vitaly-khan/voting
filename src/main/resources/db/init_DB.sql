DROP TABLE vote IF EXISTS;
DROP TABLE dish IF EXISTS;
DROP TABLE menu IF EXISTS;
DROP TABLE restaurant IF EXISTS;
DROP TABLE user IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE user
(
    id         INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name       VARCHAR(255)               NOT NULL,
    email      VARCHAR(255)               NOT NULL,
    password   VARCHAR(255)               NOT NULL,
    role       VARCHAR(12) DEFAULT 'USER' NOT NULL,
    registered TIMESTAMP   DEFAULT now()  NOT NULL,
    enabled    BOOLEAN     DEFAULT TRUE   NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON user (email);

-- No need for User_Role table,
-- as we have only 2 DIFFERENT kind of users: ADMIN and REGULAR_USER (String)
-- According to specification there is no need for Collection of roles.

CREATE TABLE restaurant
(
    id      INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name    VARCHAR(255)         NOT NULL UNIQUE,
    enabled BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE menu
(
    id            INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    date          DATE                 NOT NULL,
    restaurant_id INTEGER              NOT NULL,
    enabled       BOOLEAN DEFAULT TRUE NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX menu_unique_date_restaurant_id_idx ON menu (date, restaurant_id);

CREATE TABLE dish
(
    id      INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    price   INTEGER      NOT NULL,
    menu_id INTEGER      NOT NULL,
    enabled BOOLEAN DEFAULT TRUE NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX dish_unique_menu_id_name_idx ON dish (menu_id, name);

CREATE TABLE vote
(
    id      INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    date    DATE    NOT NULL,
    menu_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    enabled BOOLEAN DEFAULT TRUE NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX vote_unique_date_user_id ON vote (date, user_id);