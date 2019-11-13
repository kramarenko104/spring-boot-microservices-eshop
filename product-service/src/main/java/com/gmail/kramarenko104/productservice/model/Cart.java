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
public class Cart implements Serializable {

    private long cartId;
    private User user;
    private int itemsCount;
    private int totalSum;
    private Map<Product, Integer> products;

    public Cart() {
        itemsCount = 0;
        totalSum = 0;
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", user=" + user +
                ", itemsCount=" + itemsCount +
                ", totalSum=" + totalSum +
                ", products=" + Arrays.asList(products) + "}";
    }
}
