----------------------------------------- ## GENERATED FOR RegistrationControllerIntTest ## --------------------------------
insert into users(id,active, email, password, username) values(1000,1,"johndoe@mock.com","j7aad3","johndoe");
insert into customer(id,first_name, last_name, user_id) values(1000,"John","Doe",(SELECT id FROM users WHERE email="johndoe@mock.com"));
insert into cart(id, total_price, customer_id) values(1000,0,(SELECT id FROM customer WHERE user_id=(SELECT id FROM users WHERE email="johndoe@mock.com")));

insert into users(id,active, email, password, username) values(2000,1,"billking@mock.com","j7aad3","billking");
insert into customer(id,first_name, last_name, user_id) values(2000,"Bill","King",(SELECT id FROM users WHERE email="billking@mock.com"));
insert into cart(id, total_price, customer_id) values(2000,0,(SELECT id FROM customer WHERE user_id=(SELECT id FROM users WHERE email="billking@mock.com")));

