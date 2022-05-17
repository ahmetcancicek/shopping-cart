----------------------------------------- ## GENERATED FOR ROLE ## --------------------------------
INSERT IGNORE INTO role (id, name) values (1, "USER");
INSERT IGNORE INTO role (id, name) values (2, "ADMIN");

----------------------------------------- ## GENERATED FOR AUTH ## --------------------------------
INSERT IGNORE INTO users(id,active, email, password, username) VALUES(1,1,"georgehouse@email.com","$2a$10$E.nGzmuccDIDu6ywkiZGXezuw/HulzC.czympJow0lAN.EGtavCeu","georgehouse");
INSERT IGNORE INTO customer(id,first_name, last_name, user_id) VALUES(1,"George","House",(SELECT id FROM users WHERE email="georgehouse@email.com"));
INSERT IGNORE INTO cart(id, total_price, customer_id) VALUES(1,0,(SELECT id FROM customer WHERE user_id=(SELECT id FROM users WHERE email="georgehouse@email.com")));
INSERT IGNORE INTO users_roles (user_id, roles_id) VALUES ((SELECT id FROM users WHERE username="georgehouse"), (SELECT id FROM role WHERE name="ADMIN"));