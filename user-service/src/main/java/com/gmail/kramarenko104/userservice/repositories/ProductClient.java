package com.gmail.kramarenko104.userservice.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient (name = "product-service", fallback = ProductFallback.class)
public interface ProductClient {

    @GetMapping("/products")
    HttpEntity<List<String>> getAllProducts();
}
