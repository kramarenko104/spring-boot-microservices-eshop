package com.gmail.kramarenko104.cartservice.repositories;

import com.gmail.kramarenko104.cartservice.model.Cart;
import com.gmail.kramarenko104.cartservice.model.Product;

import java.util.List;

public interface CartRepo{

    Cart createCart(long userId);

    void addProduct(long userId, Product product, int quantity);

    void removeProduct(long userId, Product product, int quantity);

    List<Cart> getAllCarts();

    Cart getCartByUserId(long userId);

    void clearCartByUserId(long userId);

}