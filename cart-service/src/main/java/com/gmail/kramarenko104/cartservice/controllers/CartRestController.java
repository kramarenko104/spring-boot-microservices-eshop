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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class CartRestController {

    private static Logger logger = LoggerFactory.getLogger(CartRestController.class);
    @Autowired
    private CartServiceImpl cartService;

    public CartRestController() {
    }

    @GetMapping("/users")
    public List<String> getAllCarts() {
        return (List<String>)(cartService.getAllCarts()
                .stream()
                .map(cart -> cart.toString())
                .collect(Collectors.toList()));
    }

    @GetMapping("/users/{userId}")
    public String getCart(@PathVariable("userId") long userId) {
        return getCartPoducts(userId);
    }

    @HystrixCommand(fallbackMethod = "fallbackProcessor")
    @PostMapping(value = "/users/{userId}/products/{productId}", params = {"quantity"})
    public String addProduct(@PathVariable("userId") long userId,
                                 @PathVariable("productId") long productId,
                                 @RequestParam("quantity") int quantity) {
        cartService.addProduct(userId, productId, quantity);
        return getCartPoducts(userId);
    }

    @HystrixCommand(fallbackMethod = "fallbackProcessor")
    @DeleteMapping(value = "/users/{userId}/products/{productId}", params = {"quantity"})
    public String removeProduct(@PathVariable("userId") long userId,
                                    @PathVariable("productId") long productId,
                                    @RequestParam("quantity") int quantity) {
        cartService.removeProduct(userId, productId, quantity);
        return getCartPoducts(userId);
    }

    private String fallbackProcessor(long userId,
                                           long productId,
                                           int quantity) {
        logger.warn("Some problems knocking to microservice 'product-service'. Try a bit later.");
        return new String();
    }

    @DeleteMapping("/users/{userId}")
    public HttpStatus clearCartByUserId(@PathVariable("userId") long userId) {
        cartService.clearCartByUserId(userId);
        return HttpStatus.OK;
    }

    @PostMapping("/users/{userId}")
    public HttpEntity<String> createCart(@PathVariable("userId") long userId) {
        Cart newCart = cartService.createCart(userId);
        return new ResponseEntity<>(newCart.toString(), HttpStatus.CREATED);
    }

    private String getCartPoducts(long userId){
        return (String)(cartService.getCartByUserId(userId).toString());
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