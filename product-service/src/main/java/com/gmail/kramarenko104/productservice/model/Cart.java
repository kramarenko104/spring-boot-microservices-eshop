package com.gmail.kramarenko104.productservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter @Setter
@EqualsAndHashCode
@Table(name = "carts")
@Access(value = AccessType.FIELD)
@DynamicUpdate
@NamedQuery(name="GET_CART_BY_USERID", query = "from Cart c where c.user.user_id = :user_id")
@NamedQuery(name="GET_ALL_CARTS", query = "from Cart c")
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cart_id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    @NotNull
    private User user;

    @Transient
    private int itemsCount;

    @Transient
    private int totalSum;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "carts_products", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "product_id", updatable = false)
    @Column(name = "quantity")
    @OrderColumn (name = "cart_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Map<Product, Integer> products;

    public Cart() {
        itemsCount = 0;
        totalSum = 0;
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cart_id +
                ", user=" + user.toString() +
                ", itemsCount=" + itemsCount +
                ", totalSum=" + totalSum +
                ", products=" + Arrays.asList(products) + "}";
    }
}
