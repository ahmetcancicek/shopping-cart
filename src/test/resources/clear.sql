----------------------------------------- ## DELETED FOR RegistrationControllerIntTest ## --------------------------------
DELETE FROM cart_item WHERE ID=2000;
DELETE FROM cart WHERE ID=1000 OR ID=2000;
DELETE FROM customer WHERE ID=1000 OR ID=2000;
DELETE FROM users WHERE ID=1000 OR ID=2000;

----------------------------------------- ## DELETED FOR ProductontrollerIntTest ## --------------------------------
DELETE FROM product WHERE ID=3000;
DELETE FROM product WHERE ID=3001;