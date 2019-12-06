# spring-boot-microservices-eshop
Spring Boot + Spring Cloud App + Kafka
- Spring Boot 2.2.1
- Spring Data JPA
- Spring Cloud Netflix (Eureka, Hystrix, Config server, Ribbon, Turbine dashboard)
- Kafka 2.3.3
- Swagger2 documentation API
- MySQL Server 5.7.23 
- HikariCP 3.1.0
- Lombok 1.16.22
- Gson 2.8.5
- slf4j 1.7.5, log4j 1.2.17
- Maven
- JDK 1.8

# Functionality description:
- 4 Domain microservices: user-service, product-service, cart-service, order-service
- discover-service is Eureka server (port 8761)
- config-server (port 8888) loads application profiles from gitHub resource
- Turbine dashboard runs on port 7777. Run it: http://localhost:7777/hystrix => http://localhost:7777/turbine.stream?cluster=default
- Hystrix, LoadBalancer are enabled for cart-service, order-service that call other microservices
- Info concerning actions with user's cart (add/remove product) is sent to Kafka
- Check Kafka Consumer work: http://localhost:9000/kafka/receive 
(see screenshots/Check_Kafka_Consumer_Group_from_Postman.png).
- - Run command before this request to see topic from beginning (stop kafka-service before this):
- - kafka-consumer-groups --bootstrap-server localhost:9092 --group test --reset-offsets --to-earliest --execute -topic julia_topic
- Messages with cart's actions are sent to Kafka topic with key=userId to collect all info concerning the same user in the same partition
- REST requests for cart-service:
- - add {quantity} of product with {productId} to cart for user with {userId}:
POST localhost:8094/cart/users/{userId}/products/{productId}?quantity={quantity}
- - dlete {quantity} of product with {productId} from cart for user with {userId}:
DELETE localhost:8094/cart/users/{userId}/products/{productId}?quantity={quantity}
- - create/deleted cart for user with {userId}:
POST/DELETE localhost:8094/cart/users/{userId}
- REST requests for user-service:
- - get all users: GET localhost:8089/users
- - get user by Id: GET localhost:8093/users/{userId}
- - get user by login: GET localhost:8093/users?login={login}
- REST requests for product-service:
- - get all products: GET localhost:8092/products
- - get product by Id: GET localhost:8092/products/{productId}
- - get product by category: GET localhost:8092/products?category={category}
- - delete product by Id: DELETE localhost:8092/products/{productId}
- REST requests for order-service:
- - get all orders for user with userId: GET localhost:8091/orders/{userId}
- - delete all orders for user with userId: DELETE localhost:8091/orders/{userId}
