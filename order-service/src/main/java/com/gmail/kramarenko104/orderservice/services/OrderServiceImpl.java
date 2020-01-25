package com.gmail.kramarenko104.orderservice.services;

import com.gmail.kramarenko104.orderservice.model.Order;
import com.gmail.kramarenko104.orderservice.model.Product;
import com.gmail.kramarenko104.orderservice.model.User;
import com.gmail.kramarenko104.orderservice.repositories.OrderRepoImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final static String PROCESSED_ORDER = "ordered";

    private String userServiceURL;

    private OrderRepoImpl orderRepo;

    private RestTemplate restTemplate;

    @Autowired
    public OrderServiceImpl(RestTemplate restTemplate,
                            OrderRepoImpl orderRepo,
                           @Value ("${user-service-url}") String userServiceURL) {
        this.restTemplate = restTemplate;
        this.orderRepo = orderRepo;
        this.userServiceURL = userServiceURL;
    }

    @Override
    public void deleteAllOrders(long userId) {
        orderRepo.deleteAllOrdersForUser(userId);
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallbackOrder")
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    public Order createOrder(long userId, Map<Product, Integer> products) {
        long newOrderNumber = orderRepo.getNewOrderNumber();
        // createProduct the new order on the base of Cart
        Order newOrder = new Order();
        newOrder.setUser(restTemplate.getForObject(userServiceURL + userId, User.class));
        newOrder.setOrder_number(newOrderNumber);
        newOrder.setProducts(products);
        newOrder.setStatus(PROCESSED_ORDER);
        int totalSum = 0;
        int itemsCount = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            itemsCount += entry.getValue();
            totalSum += entry.getValue() * entry.getKey().getPrice();
        }
        newOrder.setTotalSum(totalSum);
        newOrder.setItemsCount(itemsCount);
        return orderRepo.createOrder(newOrder);
    }

    public Order fallbackOrder(long userId, Map<Product, Integer> products) {
        Order order = new Order();
        order.setStatus("NOT_CREATED");
        return order;
    }

    @Override
    public List<Order> getAllOrdersByUserId(long userId) {
        return orderRepo.getAllOrdersByUserId(userId);
    }

    @Override
    public Order getLastOrderByUserId(long userId) {
        return orderRepo.getLastOrderByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.getAllOrders();
    }
}
