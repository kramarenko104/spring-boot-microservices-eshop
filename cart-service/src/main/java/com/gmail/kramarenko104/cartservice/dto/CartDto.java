package com.gmail.kramarenko104.cartservice.dto;

import com.gmail.kramarenko104.cartservice.model.Product;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

@Setter
public class CartDto {

    private long user_id;
    private int itemsCount;
    private int totalSum;
    private Map<Product, Integer> products;

    public CartDto(long userId) {
        this.user_id = userId;
        itemsCount = 0;
        totalSum = 0;
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId:" + user_id +
                ", itemsCount:" + itemsCount +
                ", totalSum:" + totalSum +
                '}';
    }
}
