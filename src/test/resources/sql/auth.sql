
INSERT INTO role(id, name) VALUES (1, 'ROLE_USER');

INSERT INTO role(id, name) VALUES (2, 'ROLE_ADMIN');



INSERT INTO users(id, active, email, password, username) VALUES (1000, 1, 'lucycar@email.com', '$2a$10$OOmD41D50F4SufvNJN.Sde/Y/RjujIiDIBCON0t0.5cHRZlbYWhpq', 'lucycar');

INSERT INTO customer(id, first_name, last_name, user_id) VALUES (1000, 'Lucy', 'Car', (SELECT id FROM users WHERE email = 'lucycar@email.com'));

INSERT INTO cart(id, total_price, customer_id) VALUES (1000, 0, (SELECT id FROM customer WHERE user_id = (SELECT id FROM users WHERE email = 'lucycar@email.com')));

INSERT INTO users_roles (user_id, roles_id) VALUES ((SELECT id FROM users WHERE username = 'lucycar'), (SELECT id FROM role WHERE name = 'ROLE_USER'));



INSERT INTO users(id, active, email, password, username) VALUES (2000, 1, 'billking@email.com', '$2a$10$OOmD41D50F4SufvNJN.Sde/Y/RjujIiDIBCON0t0.5cHRZlbYWhpq', 'billking');

INSERT INTO customer(id, first_name, last_name, user_id) VALUES (2000, 'Bill', 'King', (SELECT id FROM users WHERE email = 'billking@email.com'));

INSERT INTO cart(id, total_price, customer_id) VALUES (2000, 0, (SELECT id FROM customer WHERE user_id = (SELECT id FROM users WHERE email = 'billking@email.com')));

INSERT INTO users_roles (user_id, roles_id) VALUES ((SELECT id FROM users WHERE username = 'billking'), (SELECT id FROM role WHERE name = 'ROLE_ADMIN'));