----------------------------------------- ## GENERATED FOR ROLE ## --------------------------------
insert into role (id, role) values (1, "USER");
insert into role (id, role) values (2, "ADMIN");

----------------------------------------- ## GENERATED FOR PRODUCT ## --------------------------------
insert into product (id, serial_number, description, name, price, quantity) values (3000, "KMNA239", "Apple iPhone 13 PRO", "iPhone 13 PRO", 1000, 5);
insert into product (id, serial_number, description, name, price, quantity) values (3001, "PADMA232", "Samsung Galaxy S22", "Galaxy S22", 1100, 3);

----------------------------------------- ## GENERATED FOR USER and CUSTOMER ## --------------------------------
insert into users(id,active, email, password, username) values(1000,1,"lucycar@email.com","$2a$10$E.nGzmuccDIDu6ywkiZGXezuw/HulzC.czympJow0lAN.EGtavCeu","lucycar");
insert into customer(id,first_name, last_name, user_id) values(1000,"Lucy","Car",(SELECT id FROM users WHERE email="lucycar@email.com"));
insert into cart(id, total_price, customer_id) values(1000,0,(SELECT id FROM customer WHERE user_id=(SELECT id FROM users WHERE email="lucycar@email.com")));
insert into users_roles (user_id, roles_id) values ((SELECT id FROM users WHERE username="lucycar"), (SELECT id FROM role WHERE role="ADMIN"));


insert into users(id,active, email, password, username) values(2000,1,"billking@email.com","2a$10$E.nGzmuccDIDu6ywkiZGXezuw/HulzC.czympJow0lAN.EGtavCeu","billking");
insert into customer(id,first_name, last_name, user_id) values(2000,"Bill","King",(SELECT id FROM users WHERE email="billking@email.com"));
insert into cart(id, total_price, customer_id) values(2000,0,(SELECT id FROM customer WHERE user_id=(SELECT id FROM users WHERE email="billking@email.com")));
insert into cart_item (id, price, quantity, cart_id, product_id) values (2000,1000,1,
                                                                         (SELECT id FROM cart WHERE customer_id=((SELECT id FROM users WHERE email="billking@email.com"))),
                                                                         (SELECT id FROM product WHERE serial_number='KMNA239'));
insert into users_roles (user_id, roles_id) values ((SELECT id FROM users WHERE username="lucycar"), (SELECT id FROM role WHERE role="USER"));

