--liquibase formatted sql

--changeset bulka:1
CREATE TABLE IF NOT EXISTS accounts
(
    id        VARCHAR(255) PRIMARY KEY NOT NULL,
    username  text UNIQUE       NOT NULL,
    email     VARCHAR(255) UNIQUE      NOT NULL,
    password  VARCHAR(255)             NOT NULL,
    is_enable BOOLEAN                  NOT NULL

);

--changeset bulka:2 context:!test
INSERT INTO accounts (id, username, email, password, is_enable)
VALUES (1, 'Alex', 'Alex@gmail', 'Alex1', true);
INSERT INTO accounts (id, username, email, password, is_enable)
VALUES (2, 'Dima', 'Dima@gmail', 'Dima1', true);
INSERT INTO accounts (id, username, email, password, is_enable)
VALUES (3, 'Anna', 'Anna@gmail', 'Anna1', true);


--changeset bulka:3
CREATE TABLE IF NOT EXISTS roles
(
    id         SERIAL PRIMARY KEY                    NOT NULL,
    name       TEXT,
    account_id VARCHAR(255) REFERENCES accounts (id) NOT NULL
);

--changeset bulka:4 context:!test
INSERT INTO roles (name, account_id)
VALUES ('USER', 1);
INSERT INTO roles (name, account_id)
VALUES ('ADMIN', 1);
INSERT INTO roles (name, account_id)
VALUES ('BOSS', 3);

-- --changeset bulka:5
-- CREATE TABLE IF NOT EXISTS accounts_roles
-- (
--     accounts_id INT REFERENCES accounts(id),
--     roles_id    INT REFERENCES roles (id)
-- );

-- SELECT id FROM accounts WHERE EXISTS(SELECT * FROM accounts WHERE accounts.username = '{}'OR email = '{}');
-- INSERT INTO accounts (id, username, email, password, is_enable, role_id) VALUES (?,?,?,?,?,?);
-- SELECT * FROM accounts
-- JOIN accounts_roles ar ON accounts.id = ar.accounts_id
-- JOIN roles r on ar.roles_id = r.id
-- WHERE accounts.username = ?