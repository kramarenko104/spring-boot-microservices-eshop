CREATE DATABASE IF NOT EXISTS eshopdb CHARACTER SET utf8 COLLATE utf8_general_ci;
USE eshopdb;

CREATE TABLE IF NOT EXISTS orders
(
	order_Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	orderNumber INT NOT NULL,
	user_id INT NOT NULL,
	status VARCHAR(20),
	FOREIGN KEY (user_id) REFERENCES users (user_id)
) 
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS orders_products
(
	order_id INT NOT NULL,
	product_id INT NOT NULL,
	quantity INT,
	PRIMARY KEY (order_id, product_id),
	FOREIGN KEY (order_id) REFERENCES orders (order_id),
	FOREIGN KEY (product_id) REFERENCES products (product_id)
) 
ENGINE = InnoDB;
