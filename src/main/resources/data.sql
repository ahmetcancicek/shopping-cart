INSERT IGNORE INTO role(id, name)
VALUES (1, 'ROLE_USER');

INSERT IGNORE INTO role(id, name)
VALUES (2, 'ROLE_ADMIN');

INSERT IGNORE INTO users(id, active, email, password, username)
VALUES (1, 1, 'georgehouse@email.com', '$2a$10$OOmD41D50F4SufvNJN.Sde/Y/RjujIiDIBCON0t0.5cHRZlbYWhpq', 'georgehouse');

INSERT IGNORE INTO customer(id, first_name, last_name, user_id)
VALUES (1, 'George', 'House', (SELECT id FROM users WHERE email = 'georgehouse@email.com'));

INSERT IGNORE INTO cart(id, total_price, customer_id)
VALUES (1, 0, (SELECT id FROM customer WHERE user_id = (SELECT id FROM users WHERE email = 'georgehouse@email.com')));

INSERT IGNORE INTO users_roles (user_id, roles_id)
VALUES ((SELECT id FROM users WHERE username = 'georgehouse'), (SELECT id FROM role WHERE name = 'ROLE_ADMIN'));

