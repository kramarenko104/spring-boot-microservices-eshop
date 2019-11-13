package com.gmail.kramarenko104.productservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
public class User implements Serializable {

    private long userId;
    private String login;
    private String password;
    private String name;
    private String address;
    private String comment;
    private Cart cart;
    private List<Order> userOrders;
    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "userId:" + userId + ", " +
                "login:'" + login + "', name:'" + name + "', roles: " + roles + "}";
    }
}