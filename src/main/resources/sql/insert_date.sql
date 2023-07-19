INSERT INTO roles (id, description, title)
SELECT 1, 'Роль пользователя', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 1)
UNION ALL
SELECT 2, 'Роль менеджера', 'MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 2);

-- Виды банковских счетов (types)
drop sequence types_seq;
create sequence types_seq;
alter sequence types_seq owner to postgres;

truncate table types cascade;

INSERT INTO types (id, created_by, created_when, title, description)
VALUES (nextval('types_seq'), 'admin', '2022-11-15 13:51:08.314682', 'Вклад1', 'Выгодный вклад с ежемесячной капитализацией процентов');
INSERT INTO types (id, created_by, created_when, title, description)
VALUES (nextval('types_seq'), 'admin', '2022-11-15 13:51:08.314682', 'Вклад2', 'Вклад с максимальным доходом');
INSERT INTO types (id, created_by, created_when, title, description)
VALUES (nextval('types_seq'), 'admin', '2022-11-15 13:51:08.314682', 'Текущий счет', 'Счет для распоряжения с помощью банковской карты');
INSERT INTO types (id, created_by, created_when, title, description)
VALUES (nextval('types_seq'), 'admin', '2022-11-15 13:51:08.314682', 'Кредит1', 'Счет гашения кредитной задолженности');
INSERT INTO types (id, created_by, created_when, title, description)
VALUES (nextval('types_seq'), 'admin', '2022-11-15 13:51:08.314682', 'Ипотека1', 'Счет для гашения ипотечной задолженности');

-- Пользователи
drop sequence users_seq;
create sequence users_seq;
alter sequence users_seq owner to postgres;

truncate table users cascade;

INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2023-07-01 13:51:08.314682', 'evgen', 'evgen', 'Евгений', 'Юрьевич', 'Семенов', '1989-01-01', '+74910382990', 'г. Новосибирск, ул. Новая, 15, кв. 124', '1', 'evgen@mail.ru');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2023-07-01 13:51:08.314682', 'lev', 'lev', 'Лев', 'Иванович', 'Романов', '1968-03-03', '+79005457407', 'г. Новосибирск, ул. Зорге, 132', '1', 'lev1968@mail.ru');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2023-07-02 13:51:08.314682', 'nina', 'nina', 'Нина', 'Евгеньевна', 'Семенова', '1993-01-01', '+79006510152', 'г. Новосибирск, ул. Новая, 15, кв. 124', '1', 'alex1993@gmail.com');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2023-07-03 13:51:08.314682', 'natali', 'natali', 'Наталья', 'Николаевна', 'Казакова', '2003-12-01', '+79003272088', 'г. Новосибирск, ул. Ленина, 18, кв.3', '1', 'natali@gmail.com');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2022-11-15 13:51:08.314682', 'marina', 'marina', 'Марина', 'Петровна', 'Палкина', '1998-03-11', '+74957609115', 'г. Новосибирск, Красный проспект, 1, кв. 115', '1', 'marina@ya.ru');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2022-11-15 13:51:08.314682', 'yana', 'yana', 'Яна', 'Валерьевна', 'Князева', '2004-12-10', '+79165732869', 'г. Новосибирск, ул. Красина, 26, кв. 11', '1', 'yanayana@ya.ru');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2022-11-15 13:51:08.314682', 'ilya', 'ilya', 'Илья', 'Евгеньевич', 'Лаврентьев', '1965-03-06', '+79638281086', 'г. Новосибирск, ул. Линейная, 256', '1', 'ilya0306@gmail.com');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2022-11-15 13:51:08.314682', 'elena', 'elena', 'Елена', 'Михайловна', 'Ульянова', '1969-12-04', '+74955207511', 'г. Новосибирск, ул. Коммунистическая, 12а, кв.98', '1', 'elena@mail.ru');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2022-11-15 13:51:08.314682', 'pavel', 'pavel', 'Павел', 'Павлович', 'Родионов', '2000-12-10', '+79000414350', 'г. Новосибирск, ул. Горького, 13', '1', 'pavel@gmail.com');
INSERT INTO users (id, created_by, created_when, login, password, first_name, middle_name, last_name, birth_date, phone, address, role_id, email)
VALUES (nextval('users_seq'), 'admin', '2022-11-15 13:51:08.314682', 'nikita', 'nikita', 'Никита', 'Семенович', 'Попов', '1972-12-01', '+74822719722', 'г. Новосибирск, Красина, 95', '1', 'nikita@mail.ru');

-- Банковские счета
drop sequence bank_accounts_seq;
create sequence bank_accounts_seq;
alter sequence bank_accounts_seq owner to postgres;

truncate table bank_accounts cascade;

INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, ending_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-07-01 13:51:08.314682', '2', '2', '42305810099910123456', '55000', '2023-07-01 13:51:08.314682', '2023-09-04 00:00:00.000000', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, ending_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-07-04 13:51:08.314682', '1', '1', '42306810099910198456', '15000', '2023-07-04 13:51:08.314682', '2024-10-04 00:00:00.000000', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-07-04 13:51:08.314682', '2', '3', '40817810099910120159', '3000', '2023-07-04 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-07-04 13:51:08.314682', '4', '3', '40817810099910120148', '16000', '2023-07-04 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, ending_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-07-05 13:51:08.314682', '5', '3', '42305810099910195147', '23000', '2023-07-05 13:51:08.314682', '2023-09-04 00:00:00.000000', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-07-07 13:51:08.314682', '6', '3', '40817810099910120951', '51000', '2023-07-07 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-04-04 13:51:08.314682', '7', '3', '40817810099910185746', '7000', '2023-04-04 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-04-04 13:51:08.314682', '8', '3', '40817810099910128854', '23000', '2023-04-04 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-04-04 13:51:08.314682', '9', '3', '40817810099910129584', '12000', '2023-04-04 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-04-04 13:51:08.314682', '10', '3', '40817810099910121010', '87000', '2023-04-04 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-04-04 13:51:08.314682', '10', '3', '40817810099910122547', '6000', '2023-04-04 13:51:08.314682', 'false');
INSERT INTO bank_accounts (id, created_by, created_when, user_id, type_id, account_number, balance, opening_date, ending_date, is_closed)
VALUES (nextval('bank_accounts_seq'), 'admin', '2023-04-04 13:51:08.314682', '10', '3', '40817810099910130211', '0', '2023-04-04 13:51:08.314682', '2023-06-04 13:51:08.314682', 'true');

-- Транзакции
drop sequence transactions_seq;
create sequence transactions_seq;
alter sequence transactions_seq owner to postgres;

truncate table transactions cascade;

INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '1', '2', '2500', 'Пополнение', '2023-07-01');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '4', '3', '1000', 'Пополнение', '2023-07-01');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '5', '3', '3000', 'Пополнение', '2023-07-02');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '5', '6', '1500', 'Пополнение', '2023-07-02');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '8', '7', '7000', 'Пополнение', '2023-07-02');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '10', '11', '5300', 'Пополнение', '2023-07-02');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '11', '2', '2500', 'Пополнение', '2023-07-03');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '9', '8', '1000', 'Пополнение', '2023-07-03');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '4', '11', '2000', 'Пополнение', '2023-07-04');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '1', '7', '2500', 'Пополнение', '2023-07-05');
INSERT INTO transactions (id, created_by, created_when, bank_account_id_from, bank_account_id_to, amount, purpose, date)
VALUES (nextval('transactions_seq'), 'admin', '2022-11-15 13:51:08.314682', '1', '2', '4000', 'Пополнение', '2023-07-05');






