package com.gmail.kramarenko104.orderservice.repositories;

import com.gmail.kramarenko104.orderservice.model.Order;

import java.util.List;

public interface OrderRepo {

    Order createOrder(Order order);

    List<Order> getAllOrdersByUserId(long userId);

    void deleteAllOrdersForUser(long userId);

    Order getLastOrderByUserId(long userId);

    Order update(Order newOrder);

    void delete(long orderId);

    List<Order> getAll();
}