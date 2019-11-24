CREATE DATABASE IF NOT EXISTS eshopdb CHARACTER SET utf8 COLLATE utf8_general_ci;
USE eshopdb;

CREATE TABLE IF NOT EXISTS users
(
	user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	login VARCHAR (100) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	name VARCHAR(50) NOT NULL,
	address VARCHAR(50) NOT NULL,
	comment VARCHAR(100)
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS roles
(
  role_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) default 'ROLE_USER' NOT NULL
) 
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS user_roles
(
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (user_id),
  FOREIGN KEY (role_id) REFERENCES roles (role_id),
  UNIQUE (user_id, role_id)
) 
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS products
(
	product_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL UNIQUE,
	category int NOT NULL,
	price int NOT NULL,
	description VARCHAR(300),
	image VARCHAR(100)
) 
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS carts
(
	cart_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id int NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users (user_id)
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS carts_products
(
	cart_id INT NOT NULL,
	product_id INT NOT NULL,
	quantity INT,
	PRIMARY KEY (cart_id, product_id),
	FOREIGN KEY (product_id) REFERENCES products (product_id),
	FOREIGN KEY (cart_id) REFERENCES carts (cart_id)
) 
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS orders
(
	order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	order_number INT NOT NULL,
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
