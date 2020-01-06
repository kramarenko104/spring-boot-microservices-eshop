package com.gmail.kramarenko104.userservice.repositories;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

public class ProductFallback implements ProductClient{
    @Override
    public HttpEntity<List<String>> getAllProducts() {
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }
}
