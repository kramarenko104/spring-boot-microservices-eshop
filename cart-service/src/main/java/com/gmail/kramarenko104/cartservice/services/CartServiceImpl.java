package com.gmail.kramarenko104.cartservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.kramarenko104.cartservice.model.Cart;
import com.gmail.kramarenko104.cartservice.model.Product;
import com.gmail.kramarenko104.cartservice.repositories.CartRepoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepoImpl cartRepo;

    @Autowired
    private RestTemplate restTemplate;

    public CartServiceImpl() {
    }

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
        //send message to Kafka about action 'add product to cart'
        sendMessageToKafka(userId, productToAdd, quantity, "ADDED");
    }

    @Override
    public void removeProduct(long userId, long productId, int quantity) {
        Product productToRemove = restTemplate.getForObject("http://product-service/products/api/" + productId, Product.class);
        cartRepo.removeProduct(userId, productToRemove, quantity);
        //send message to Kafka about action 'remove product from cart'
        sendMessageToKafka(userId, productToRemove, quantity, "REMOVED");
    }

    private void sendMessageToKafka(long userId, Product product, int quantity, String action) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("cartId", Long.toString(getCartByUserId(userId).getCart_id()));
        messageMap.put("userId", Long.toString(userId));
        messageMap.put("action", action);
        messageMap.put("product", product.toString());
        messageMap.put("quantity", Integer.toString(quantity));
        messageMap.put("result", getCartByUserId(userId).toString());
        String jsonMessage = "";
        try {
            jsonMessage = mapper.writeValueAsString(messageMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.debug("[eshop] send jsonMessage to Kafka: " + jsonMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(jsonMessage, headers);
        try {
            String response = restTemplate.exchange("http://kafka-service/kafka/send?key=cart", HttpMethod.POST, entity, String.class).getBody();
            logger.debug("[eshop] got RESPONSE from Kafka: " + response);
        } catch (HttpClientErrorException e) {
            logger.debug("[eshop] got CLIENT exception: " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            logger.debug("[eshop] got SERVER exception: " + e.getResponseBodyAsString());
        }
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
