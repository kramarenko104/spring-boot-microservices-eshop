package com.gmail.kramarenko104.orderservice.controllers;

import com.gmail.kramarenko104.orderservice.model.Order;
import com.gmail.kramarenko104.orderservice.model.Product;
import com.gmail.kramarenko104.orderservice.model.User;
import com.gmail.kramarenko104.orderservice.repositories.OrderRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    public OrderRestController(){}

    private final static String PROCESSED_ORDER = "ordered";

    @Autowired
    private OrderRepoImpl orderRepo;

    @Autowired
    private RestTemplate restTemplate;

    @DeleteMapping("/{userId}")
    public void deleteAllOrders(@PathVariable("userId") long userId) {
        orderRepo.deleteAllOrdersForUser(userId);
    }

    @GetMapping("/{userId}")
    public HttpEntity<List<Order>> getAllOrders(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(orderRepo.getAll(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public HttpEntity<Order> getLastOrder(@PathVariable("userId") long userId) {
        Order order = orderRepo.getLastOrderByUserId(userId);
        if (order != null) {
            order = recalculateOrder(order);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public HttpEntity<Order> createOrder(@PathVariable("userId") long userId,
                             @RequestParam("products") Map<Product, Integer> products) {
        long newOrderNumber = orderRepo.getNewOrderNumber();
        // createProduct the new order on the base of Cart
        Order newOrder = new Order();
        User currentUser = restTemplate.getForObject("http://user-service/users/" + userId, User.class);
        newOrder.setUser(currentUser);
        newOrder.setOrderNumber(newOrderNumber);
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
        return new ResponseEntity<>(orderRepo.createOrder(newOrder), HttpStatus.CREATED);
    }

    private Order recalculateOrder(Order order) {
        Map<Product, Integer> productsInOrder = order.getProducts();
        int itemsCount = 0;
        int totalSum = 0;
        if (productsInOrder.size() > 0) {
            int quantity = 0;
            for (Map.Entry<Product, Integer> entry : productsInOrder.entrySet()) {
                quantity = entry.getValue();
                itemsCount += quantity;
                totalSum += quantity * entry.getKey().getPrice();
            }
        }
        if (itemsCount > 0) {
            order.setItemsCount(itemsCount);
            order.setTotalSum(totalSum);
        }
        return order;
    }

}
