package com.gmail.kramarenko104.userservice.models;

public enum RoleEnum {

    ROLE_ADMIN(1),
    ROLE_USER(2);

    private int role_id;

    private RoleEnum(int role_id) {
        this.role_id = role_id;
    }
}

