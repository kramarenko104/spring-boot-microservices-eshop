package com.gmail.kramarenko104.cartservice.services;

import com.gmail.kramarenko104.cartservice.model.Cart;

import java.util.List;

public interface CartService {

    Cart createCart(long userId);

    List<Cart> getAllCarts();

    Cart getCartByUserId(long userId);

    void addProduct(long cartId, long productId, int quantity);

    void removeProduct(long userId, long productId, int quantity);

    void clearCartByUserId(long Id);

}
