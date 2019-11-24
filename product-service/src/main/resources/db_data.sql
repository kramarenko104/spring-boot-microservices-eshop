USE eshopdb;
INSERT INTO users (user_id, login, password, name, address, comment) VALUES (1, 'admin', '1e8949373cf0dc044b49c9dd3ddd1835', 'Alex', 'Kiev', '-');
INSERT INTO users (user_id, login, password, name, address, comment) VALUES (2, 'alex@gmail.com', '-5039f9f6cecff39cf359c1de1fbc2b7f', 'Alexander', 'Kiev', 'call before delivery');
INSERT INTO users (user_id, login, password, name, address, comment) VALUES (3, 'julia@gmail.com', '53df562cd13c57455c2c1bbe1854458f', 'julia', 'Kiev', 'd''ont call');
INSERT INTO users (user_id, login, password, name, address, comment) VALUES (4, 'mash198@ukr.net', '53df562cd13c57455c2c1bbe1854458f', 'Maria', 'Lviv', 'd''ont call');

INSERT INTO roles (role_id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (role_id, name) VALUES (2, 'ROLE_USER');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (4, 2);

INSERT INTO products (product_id, name, category, price, description, image) VALUES (1, 'Nora Naviano Imressive dusty blue', 1, 3450, 'Blue evening dress with an embroidered bodice and a narrow belt. Model of 2019 year. Main material: atlas. Light shine of a fabric and  the romantic muffled shade of blue will submit the most dreamy girls.', 'dusty-blue-400x650_3400.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (2, 'Very berry marsala', 1, 1654, 'Very berry. Long satin dress with a guipure top, a high waistline and a boat neckline. Color: Marsala.', 'evening_dress_f1_2300.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (3, 'Dress Felicia', 1, 3400, 'Silk satin dress. The top of the dress is laced. Silk satin skirt fits perfectly on the hips due to the folds. Dresses of A-shaped cut look amazing in a satin version, especially in pastel colors: ashy rose, peach, coffee colors.', 'evening_dress_felicia_4500.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (4, 'Shoes ROSE GOLD Rock Glitter Ankle', 2, 2100, 'Comfortable stylish shoes decorated with sequins', 'baletki_1255.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (5, 'Dolce & Gabbana', 2, 3500, 'Shoes Dolce & Gabbana, velvet wine shade, decorated with rhinestones', 'Dolce & Gabbana_3500.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (6, 'Rene Caovilla', 2, 3750, 'Evening shoes Rene Caovilla, black velvet with rhinestones', 'Rene_Caovilla_4300.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (7, 'Lady bag Parfois 163918-BU', 3, 1429, 'Portugal, size 28 x 29 x 13 см', 'parfois_163918-BU.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (8, 'Lady bag Furla', 3, 1200, 'Italy, size 22 х 4,5 х 15 см', 'furla_1.jpg');
INSERT INTO products (product_id, name, category, price, description, image) VALUES (9, 'Evening clutch with rhinestones', 3, 1200, 'The outer part of the bag is completely covered with rhinestone ornament. The back of the accessory is silver brocade. The case is rigid, the metal frame is silver. The size 22 х 4,5 х 12 см', 'klatch.jpg');

INSERT INTO carts (cart_id, user_id) VALUES (1, 1);
INSERT INTO carts (cart_id, user_id) VALUES (2, 2);

INSERT INTO carts_products (cart_id, product_id, quantity) VALUES (1, 2, 1);
INSERT INTO carts_products (cart_id, product_id, quantity) VALUES (1, 1, 3);
INSERT INTO carts_products (cart_id, product_id, quantity) VALUES (2, 3, 2);

INSERT INTO orders (order_id, order_number, user_id, status) VALUES (1, 345, 1, 'ORDERED');
INSERT INTO orders (order_id, order_number, user_id, status) VALUES (2, 346, 2, 'ORDERED');

INSERT INTO orders_products (order_id, product_id, quantity) VALUES (1, 2, 1);
INSERT INTO orders_products (order_id, product_id, quantity) VALUES (1, 6, 3);
INSERT INTO orders_products (order_id, product_id, quantity) VALUES (2, 3, 2);