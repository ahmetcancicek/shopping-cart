INSERT INTO role(id, name) VALUES (1, 'ROLE_USER');
INSERT INTO role(id, name) VALUES (2, 'ROLE_ADMIN');


INSERT IGNORE INTO product (id, serial_number, description, name, price, quantity) VALUES (3000, 'KMNA239', 'Apple iPhone 13 PRO', 'iPhone 13 PRO', 1000, 5);

INSERT IGNORE INTO product (id, serial_number, description, name, price, quantity) VALUES (3001, 'PADMA232', 'Samsung Galaxy S22', 'Galaxy S22', 1100, 3);



INSERT INTO users(id, active, email, password, username) VALUES (1000, 1, 'lucycar@email.com', '$2a$10$OOmD41D50F4SufvNJN.Sde/Y/RjujIiDIBCON0t0.5cHRZlbYWhpq', 'lucycar');

INSERT INTO customer(id, first_name, last_name, user_id) VALUES (1000, 'Lucy', 'Car', (SELECT id FROM users WHERE email = 'lucycar@email.com'));

INSERT INTO cart(id, total_price, customer_id) VALUES (1000, 0, (SELECT id FROM customer WHERE user_id = (SELECT id FROM users WHERE email = 'lucycar@email.com')));

INSERT INTO cart_item (id, price, quantity, cart_id, product_id)
    VALUES (
    3000,
    1000,
    1,
    (SELECT id FROM cart WHERE customer_id = ((SELECT id FROM users WHERE email = 'lucycar@email.com'))),
    (SELECT id FROM product WHERE serial_number = 'KMNA239')
    );


INSERT INTO users_roles (user_id, roles_id) VALUES ((SELECT id FROM users WHERE username = 'lucycar'), (SELECT id FROM role WHERE name = 'ROLE_USER'));