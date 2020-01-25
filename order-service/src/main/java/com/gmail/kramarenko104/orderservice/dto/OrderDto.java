package com.gmail.kramarenko104.orderservice.dto;

import com.gmail.kramarenko104.orderservice.model.Product;
import lombok.Setter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Setter
public class OrderDto {

    private long user_id;

    private long orderNumber;

    private int itemsCount;

    private int totalSum;

    private Map<Product, Integer> products;

    public OrderDto(long userId) {
        this.user_id = userId;
        itemsCount = 0;
        totalSum = 0;
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", userId=" + user_id +
                ", itemsCount=" + itemsCount +
                ", totalSum=" + totalSum +
                ", products=[" + Arrays.asList(products) + "]}";
    }


}
