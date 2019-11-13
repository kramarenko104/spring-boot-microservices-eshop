package com.gmail.kramarenko104.orderservice.services;

import com.gmail.kramarenko104.orderservice.model.Order;
import com.gmail.kramarenko104.orderservice.model.Product;

import java.util.List;
import java.util.Map;

public interface OrderService {

    void deleteAllOrders(long userId);

    Order createOrder(long userId, Map<Product, Integer> products);

    List<Order> getAll();

    Order getLastOrderByUserId(long userId);
}
