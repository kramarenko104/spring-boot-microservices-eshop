package com.gmail.kramarenko104.orderservice.exceptions;

public class OrderNotFoundException extends RuntimeException {

    private String message;

    public OrderNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
