----------------------------------------- ## DELETED FOR USER and CUSTOMER ## --------------------------------
DELETE FROM cart_item WHERE ID=1000;
DELETE FROM cart WHERE ID=1000;
DELETE FROM users_roles WHERE user_id=1000;
DELETE FROM customer WHERE ID=1000;
DELETE FROM users WHERE ID=1000;

DELETE FROM cart_item WHERE ID=2000;
DELETE FROM cart WHERE ID=2000;
DELETE FROM users_roles WHERE user_id=2000;
DELETE FROM customer WHERE  ID=2000;
DELETE FROM users WHERE ID=2000;

----------------------------------------- ## DELETE FOR ROLE ## --------------------------------
DELETE FROM role WHERE role="USER";
DELETE FROM role WHERE role="ADMIN";

----------------------------------------- ## DELETED FOR PRODUCT ## --------------------------------
DELETE FROM product WHERE ID=3000;
DELETE FROM product WHERE ID=3001;