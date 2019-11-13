package com.gmail.kramarenko104.productservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@EqualsAndHashCode
public class Order implements Serializable {

    private long orderId;
    private long orderNumber;
    private User user;
    private String status;
    private int itemsCount;
    private int totalSum;
    private Map<Product, Integer> products;

    public Order() {
        itemsCount = 0;
        totalSum = 0;
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", user=" + user +
                ", itemsCount=" + itemsCount +
                ", totalSum=" + totalSum +
                ", products=" + Arrays.asList(products) + "}";
    }
}
