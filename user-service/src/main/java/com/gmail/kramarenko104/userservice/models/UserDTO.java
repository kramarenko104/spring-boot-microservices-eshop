package com.gmail.kramarenko104.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private long user_id;

    private String login;

    private String name;

    private String address;

    private Set<RoleEnum> roles = new HashSet<>();

}
