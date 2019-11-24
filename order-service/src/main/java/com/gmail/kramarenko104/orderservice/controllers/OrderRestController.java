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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    public OrderRestController(){}

    private final static String PROCESSED_ORDER = "ORDERED";

    @Autowired
    private OrderServiceImpl orderService;

    @DeleteMapping("/{userId}")
    public HttpStatus deleteAllOrders(@PathVariable("userId") long userId) {
        orderService.deleteAllOrders(userId);
        return HttpStatus.OK;
    }

    @GetMapping
    public HttpEntity<List<String>> getAllOrders() {
        List<String> orders = orderService.getAllOrders()
                .stream()
                .map(order -> order.toString())
                .collect(Collectors.toList());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public HttpEntity<List<String>> getAllOrdersByUserId(@PathVariable("userId") long userId) {
        List<String> orders = orderService.getAllOrdersByUserId(userId)
                .stream()
                .map(order -> order.toString())
                .collect(Collectors.toList());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    private String getLastOrder(@PathVariable("userId") long userId) {
        Order order = orderService.getLastOrderByUserId(userId);
        return order.toString();
    }

    @PostMapping("/{userId}")
    public HttpEntity<String> createOrder(@PathVariable("userId") long userId,
                             @RequestParam("products") Map<Product, Integer> products) {
        return new ResponseEntity<>(orderService.createOrder(userId, products).toString(), HttpStatus.CREATED);
    }
}
