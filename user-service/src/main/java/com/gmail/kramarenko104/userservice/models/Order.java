package com.gmail.kramarenko104.userservice.models;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(name="order_id", value="order_id", example="1", required=true)
    private long order_id;

    @Column(nullable = false, updatable = false)
    @ApiModelProperty(name="order_number", value="order_number", example="345", required=true)
    private long order_number;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @NotNull
    private User user;

    @Column(columnDefinition = "varchar(20)")
    @EqualsAndHashCode.Exclude
    @ApiModelProperty(name="status", value="order's status", example="SHIPPED", required=false)
    private String status;

    @Transient
    @ApiModelProperty(name="itemsCount", value="itemsCount in the last order", example="4",required=true)
    private int itemsCount;

    @Transient
    @ApiModelProperty(name="totalSum", value="totalSum in the last order", example="12904", required=true)
    private int totalSum;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "orders_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "product_id", updatable = false)
    @Column(name = "quantity")
    @OrderColumn (name = "order_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @NotNull
    @ApiModelProperty(name="products",
            value="products' list un the current in the last order",
            example="[{{product_id:6,name:Rene Caovilla,price:3750}:3}, {{product_id:2,name:Very berry marsala,price:1654}:1}]",
            required=true)
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
