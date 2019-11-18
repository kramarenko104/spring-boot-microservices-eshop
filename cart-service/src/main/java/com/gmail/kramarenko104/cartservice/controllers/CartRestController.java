package com.gmail.kramarenko104.cartservice.controllers;

import com.gmail.kramarenko104.cartservice.model.Cart;
import com.gmail.kramarenko104.cartservice.model.Product;
import com.gmail.kramarenko104.cartservice.services.CartServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartRestController {

    private static Logger logger = LoggerFactory.getLogger(CartRestController.class);

    @Autowired
    private CartServiceImpl cartService;

    public CartRestController() {
    }

    @GetMapping("/users")
    public HttpEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public HttpEntity<Cart> getCart(@PathVariable("userId") long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "fallbackProcessor")
    @PostMapping(value = "/users/{userId}/products/{productId}", params = {"quantity"})
    public HttpStatus addProduct(@PathVariable("userId") long userId,
                           @PathVariable("productId") long productId,
                           @RequestParam("quantity") int quantity) {
         cartService.addProduct(userId, productId, quantity);
         return HttpStatus.CREATED;
    }

    @HystrixCommand(fallbackMethod = "fallbackProcessor")
    @DeleteMapping(value = "/users/{userId}/products/{productId}", params = {"quantity"})
    public HttpStatus removeProduct(@PathVariable("userId") long userId,
                              @PathVariable("productId") long productId,
                              @RequestParam("quantity") int quantity) {
        cartService.removeProduct(userId, productId, quantity);
        return HttpStatus.OK;
    }


    @DeleteMapping("/users/{userId}")
    public HttpStatus clearCartByUserId(@PathVariable("userId") long userId) {
        cartService.clearCartByUserId(userId);
        return HttpStatus.OK;
    }

    @PostMapping("/users/{userId}")
    public HttpEntity<Cart> createCart(@PathVariable("userId") long userId) {
        Cart newCart = cartService.createCart(userId);
        return new ResponseEntity<>(newCart, HttpStatus.CREATED);
    }

    private HttpStatus fallbackProcessor(long userId,
                                   long productId,
                                   int quantity) {
        logger.warn("Some problems knocking to microservice 'product-service'. Try a bit later.");
        return HttpStatus.EXPECTATION_FAILED;
    }

    protected static Cart recalculateCart(Cart cart) {
        Map<Product, Integer> productsInCart = cart.getProducts();
        int itemsCount = 0;
        int totalSum = 0;
        if (productsInCart.size() > 0) {
            int quantity = 0;
            for (Map.Entry<Product, Integer> entry : productsInCart.entrySet()) {
                quantity = entry.getValue();
                itemsCount += quantity;
                totalSum += quantity * entry.getKey().getPrice();
            }
        }
        if (itemsCount > 0) {
            cart.setItemsCount(itemsCount);
            cart.setTotalSum(totalSum);
        }
        return cart;
    }
}
