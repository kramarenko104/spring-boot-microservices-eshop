package com.gmail.kramarenko104.orderservice.controllers;

import com.gmail.kramarenko104.orderservice.model.Order;
import com.gmail.kramarenko104.orderservice.model.Product;
import com.gmail.kramarenko104.orderservice.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    public OrderRestController(){}

    private final static String PROCESSED_ORDER = "ordered";

    @Autowired
    private OrderServiceImpl orderService;

    @DeleteMapping("/{userId}")
    public void deleteAllOrders(@PathVariable("userId") long userId) {
        orderService.deleteAllOrders(userId);
    }

    @GetMapping("/{userId}")
    public HttpEntity<List<Order>> getAllOrders(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(orderService.getAllOrdersByUserId(userId), HttpStatus.OK);
    }

    public HttpEntity<Order> getLastOrder(@PathVariable("userId") long userId) {
        Order order = orderService.getLastOrderByUserId(userId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public HttpEntity<Order> createOrder(@PathVariable("userId") long userId,
                             @RequestParam("products") Map<Product, Integer> products) {
        return new ResponseEntity<>(orderService.createOrder(userId, products), HttpStatus.CREATED);
    }
}
