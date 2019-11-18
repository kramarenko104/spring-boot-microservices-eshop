package com.gmail.kramarenko104.cartservice.repositories;

import com.gmail.kramarenko104.cartservice.exceptions.CartNotFoundException;
import com.gmail.kramarenko104.cartservice.exceptions.UserNotFoundException;
import com.gmail.kramarenko104.cartservice.model.Cart;
import com.gmail.kramarenko104.cartservice.model.Product;
import com.gmail.kramarenko104.cartservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CartRepoImpl implements CartRepo {

    private final static Logger logger = LoggerFactory.getLogger(CartRepoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Cart createCart(long userId) {
        Cart cart = null;
        try {
            User userDb = Optional.ofNullable(restTemplate.getForObject("http://user-service/user/"+userId, User.class))
                    .orElseThrow(() -> new UserNotFoundException("User with id=" + userId + "not found in DB. Cart cannot be created"));
            // make currentUser managed
            User currentUser = em.merge(userDb);
            cart = new Cart();
            cart.setUser(currentUser);
            em.persist(cart);
            logger.debug("[eshop] CartRepoImpl.createCart: NEW cart was created: " + cart);
        } catch (UserNotFoundException ex) {
            logger.debug("[eshop] CartRepoImpl.createCart: " + ex.getMessage());
        }
        return cart;
    }

    @Override
    public List<Cart> getAllCarts() {
        List<Cart> carts = new ArrayList<>();
        try {
            TypedQuery<Cart> query = em.createNamedQuery("GET_ALL_CARTS", Cart.class);
            carts = query.getResultList();
            logger.debug("[eshop] CartRepoImpl.getAllCarts: " + carts);
        } catch (NoResultException ex) {
            logger.debug("[eshop] CartRepoImpl.getCartByUserId: Any carts are not found in DB");
        }
        return carts;
    }

    @Override
    public Cart getCartByUserId(long userId) {
        Cart cart = null;
        try {
            TypedQuery<Cart> query = em.createNamedQuery("GET_CART_BY_USERID", Cart.class)
                    .setParameter("user_id", userId);
            cart = query.getSingleResult();
            logger.debug("[eshop] CartRepoImpl.getCartByUserId: return cart: " + cart);
        } catch (NoResultException ex) {
            logger.debug("[eshop] CartRepoImpl.getCartByUserId: Cart for user_id=" + userId + " not found in DB");
        }
        return cart;
    }

    @Override
    public void clearCartByUserId(long userId) {
        try {
            Cart cartToClear = Optional.ofNullable(getCartByUserId(userId))
                    .orElseThrow(() -> new CartNotFoundException("Cart for userId=" + userId + " not found in DB. Cart cannot be removed"));
            logger.debug("[eshop] CartRepoImpl.clearCartByUserId: cartToRemove: " + cartToClear);
            // let's cartId stays the same for this userId
            // just remove all products from this cart
            // for this purpose cartToClear should stay in detached status
            // so, 'cart' table won't be clear, but 'carts_products' table will be cleaned - it's expected result
            em.remove(cartToClear);
            logger.debug("[eshop] CartRepoImpl.clearCartByUSerId: cart for userId: " + userId + " was deleted");
        } catch (CartNotFoundException ex) {
            logger.debug("[eshop] CartRepoImpl.clearCartByUserId: " + ex.getMessage());
        }
    }

    @Override
    public void addProduct(long userId,
                           Product product,
                           int addQuantity) {
        logger.debug("[eshop] CartRepoImpl.addProduct: for userId: " + userId + ", product: " + product + ", quantity: " + addQuantity);
        int dbQuantity = 0;
        try {
            Cart cartDb = Optional.ofNullable(getCartByUserId(userId))
                    .orElseGet(() -> Optional.ofNullable(createCart(userId))
                            .orElseThrow(() -> new RuntimeException("cannot create cart")));
            if (cartDb.getProducts().containsKey(product)) {
                dbQuantity = cartDb.getProducts().get(product);
            }
            cartDb.getProducts().put(product, dbQuantity + addQuantity);
            em.merge(cartDb);
        } catch (Exception ex) {
            logger.debug("[eshop] CartRepoImpl.addProduct: " + ex.getMessage());
        }
    }

    @Override
    public void removeProduct(long userId,
                              Product product,
                              int rmQuantity) {
        logger.debug("[eshop] CartRepoImpl.deleteProduct: for userId: " + userId + ", product: " + product + ", quantity: " + rmQuantity);
        int dbQuantity = 0;
        try {
            Cart cartDb = Optional.ofNullable(getCartByUserId(userId))
                    .orElseThrow(() -> new CartNotFoundException("Cart for userId=" + userId + " not found in DB. Product cannot be removed"));

            if (cartDb.getProducts().containsKey(product)) {
                dbQuantity = cartDb.getProducts().get(product);
            }

            // there is already such product in dB, just update quantity
            if (dbQuantity > 0) {
                // cannot remove more then we have
                if (dbQuantity < rmQuantity) {
                    rmQuantity = dbQuantity;
                }
                if (rmQuantity != dbQuantity) {
                    logger.debug("[eshop] CartRepoImpl.deleteProduct: there is such product in dB, update quantity");
                    cartDb.getProducts().put(product, dbQuantity - rmQuantity);
                } else {
                    // there is only 1 quantity of the product, so, deleteProduct this record from DB
                    logger.debug("[eshop] CartRepoImpl.deleteProduct: there is only one product in dB, so, deleteProduct it from DB");
                    cartDb.getProducts().remove(product);
                }
                em.merge(cartDb);
            }
        } catch (CartNotFoundException ex) {
            logger.debug("[eshop] CartRepoImpl.deleteProduct: " + ex.getMessage());
        }
    }

    public Cart update(Cart newCart) {
        return em.merge(newCart);
    }

    public void delete(long cartId) {
        try {
            Cart cart = Optional.ofNullable(em.find(Cart.class, cartId))
                    .orElseThrow(() -> new EntityNotFoundException("Not found instance of Cart for id=" + cartId));
            em.remove(cart);
        } catch (EntityNotFoundException ex) {
            logger.debug(ex.getMessage());
        }
    }

}
