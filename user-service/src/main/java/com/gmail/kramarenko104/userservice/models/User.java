package com.gmail.kramarenko104.userservice.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@Table(name = "users")
@Access(value = AccessType.FIELD)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name="user_id", value="user_id", example="1", required=true)
    private long user_id;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(30)")
    @NotNull
    @ApiModelProperty(name="login", value="e-mail address as user's login", example="alex@gmail.com", required=true)
    private String login;

    @Column(nullable = false, columnDefinition = "varchar(50)")
    @NotNull
    @Length(min = 4, message = "Password should have minimum 4 symbols!")
    @ApiModelProperty(name="password", value="minimum 4 symbols", example="Gr3456", required=true)
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(50)")
    @NotNull
    @ApiModelProperty(name="name", value="users' name", example="Alex Petrenko", required=true)
    private String name;

    @Column(columnDefinition = "varchar(50)")
    @NotNull
    @ApiModelProperty(name="address", value="users' address", example="Kiev", required=false)
    private String address;

    @Column(columnDefinition = "varchar(100)")
    @ApiModelProperty(name="comment", value="users' comment", example="don't call before delivery", required=false)
    private String comment;

    @OneToOne (mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Cart cart;

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<Order> userOrders;

    @OneToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ApiModelProperty(name="roles", value="users' roles", example="[ROLE_USER, ROLE_ADMIN]", required=true)
    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "userId:" + user_id + ", " +
                "login:'" + login + "', name:'" + name + "', roles: " + roles + "}";
    }
}