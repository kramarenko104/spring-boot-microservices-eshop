package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.repositories.ProductClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    ProductClient productClient;

    @GetMapping("/products")
    @ApiOperation(value = "Get All Products", notes = "Get All Products (for admins only)", response = String.class)
    public HttpEntity<List<String>> getAllProducts(){
        return productClient.getAllProducts();
    }
}
