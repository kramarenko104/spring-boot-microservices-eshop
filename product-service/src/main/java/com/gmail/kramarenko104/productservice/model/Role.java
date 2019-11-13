package com.gmail.kramarenko104.productservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Role implements Serializable {

    private long roleId;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}