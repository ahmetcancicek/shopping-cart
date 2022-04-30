----------------------------------------- ## GENERATED FOR RegistrationControllerIntTest ## --------------------------------
insert into users(id,active, email, password, username) values(1000,1,"lucycar@email.com","9adm3av","lucycar");
insert into customer(id,first_name, last_name, user_id) values(1000,"Lucy","Car",(SELECT id FROM users WHERE email="lucycar@email.com"));
insert into cart(id, total_price, customer_id) values(1000,0,(SELECT id FROM customer WHERE user_id=(SELECT id FROM users WHERE email="lucycar@email.com")));

insert into users(id,active, email, password, username) values(2000,1,"billking@email.com","j7aad3","billking");
insert into customer(id,first_name, last_name, user_id) values(2000,"Bill","King",(SELECT id FROM users WHERE email="billking@email.com"));
insert into cart(id, total_price, customer_id) values(2000,0,(SELECT id FROM customer WHERE user_id=(SELECT id FROM users WHERE email="billking@email.com")));

----------------------------------------- ## GENERATED FOR ProductControllerIntTest ## --------------------------------
insert into product (id, serial_number, description, name, price, quantity) values (3000, "KMNA239", "Apple iPhone 13 PRO", "iPhone 13 PRO", 1000, 5);
insert into product (id, serial_number, description, name, price, quantity) values (3001, "PADMA232", "Samsung Galaxy S22", "Galaxy S22", 1100, 3);