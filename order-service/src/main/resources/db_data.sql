USE eshopdb;

INSERT INTO orders (order_id, user_id, orderNumber, status) VALUES (1, 1, 11,"ORDERED");
INSERT INTO orders (order_id, user_id, orderNumber, status) VALUES (2, 2, 12,"ORDERED");

INSERT INTO orders_products (order_id, quantity, product_id) VALUES (1, 12, 2);
INSERT INTO orders_products (order_id, quantity, product_id) VALUES (2, 5, 2);
INSERT INTO orders_products (order_id, quantity, product_id) VALUES (2, 4, 6);
