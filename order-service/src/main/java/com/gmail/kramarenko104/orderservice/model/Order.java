package com.gmail.kramarenko104.orderservice.model;

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
@Table(name = "orders")
@Access(value = AccessType.FIELD)
@DynamicUpdate
@NamedQueries(value =
        {@NamedQuery(name = "GET_ALL_ORDERS_BY_USERID", query = "from Order o where o.user.user_id = :user_id"),
        @NamedQuery(name = "GET_ALL_ORDERS", query = "from Order o"),
        @NamedQuery(name = "GET_LAST_ORDER_NUMBER", query = "select distinct max(o.order_number) as lastOrderNumber from Order o"),
        @NamedQuery(name = "GET_LAST_ORDER_BY_USERID", query = "from Order o where o.user.user_id = :user_id order by o.order_number DESC")})
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;

    @Column(nullable = false, updatable = false)
    private long order_number;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @NotNull
    private User user;

    @Column(columnDefinition = "varchar(20)")
    @EqualsAndHashCode.Exclude
    private String status;

    @Transient
    private int itemsCount;

    @Transient
    private int totalSum;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "orders_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "product_id", updatable = false)
    @Column(name = "quantity")
    @OrderColumn (name = "order_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @NotNull
    private Map<Product, Integer> products;

    public Order() {
        itemsCount = 0;
        totalSum = 0;
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_number=" + order_number +
                ", user=" + user +
                ", itemsCount=" + itemsCount +
                ", totalSum=" + totalSum +
                ", products=" + Arrays.asList(products) + "}";
    }
}
