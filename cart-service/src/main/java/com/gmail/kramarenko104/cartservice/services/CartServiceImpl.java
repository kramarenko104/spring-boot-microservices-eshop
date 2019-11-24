package com.gmail.kramarenko104.cartservice.services;

import com.gmail.kramarenko104.cartservice.model.Cart;
import com.gmail.kramarenko104.cartservice.model.Product;
import com.gmail.kramarenko104.cartservice.repositories.CartRepoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepoImpl cartRepo;

    @Autowired
    private RestTemplate restTemplate;

    public CartServiceImpl(){}

    @Override
    public Cart createCart(long userId) {
        return cartRepo.createCart(userId);
    }

    @Override
    public Cart getCartByUserId(long userId) {
        Cart cart = cartRepo.getCartByUserId(userId);
        if (cart != null && cart.getProducts().size() > 0) {
            cart = recalculateCart(cart);
        }
        logger.debug("[eshop] getCartByUserId return: " + cart);
        return cart;
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepo.getAllCarts();
    }


    @Override
    public void addProduct(long userId, long productId, int quantity) {
        Product productToAdd = restTemplate.getForObject("http://product-service/products/api/" + productId, Product.class);
        cartRepo.addProduct(userId, productToAdd, quantity);
    }

    @Override
    public void removeProduct(long userId, long productId, int quantity) {
        Product productToRemove = restTemplate.getForObject("http://product-service/products/api/" + productId, Product.class);
        cartRepo.removeProduct(userId, productToRemove, quantity);
    }

    @Override
    public void clearCartByUserId(long userId) {
        cartRepo.clearCartByUserId(userId);
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
