package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.UserDTO;
import com.gmail.kramarenko104.userservice.repositories.ProductClient;
import com.gmail.kramarenko104.userservice.services.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    ProductClient productClient;

    UserServiceImpl userService;

    @Autowired
    public AdminRestController(UserServiceImpl userService,
                               ProductClient productClient){
        this.userService = userService;
        this.productClient = productClient;
    }

    @GetMapping("/products")
    @ApiOperation(value = "Get All Products", notes = "Get All Products (for admins only)", response = String.class)
    public HttpEntity<List<String>> getAllProducts(){
        return productClient.getAllProducts();
    }

    @GetMapping("/users")
    @ApiOperation(value = "Get All Users", notes = "Get All Users (for admins only)", response = String.class)
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return userService.getAllUsers()
                .map(foundUsers -> ResponseEntity.ok(foundUsers))
                .orElse(ResponseEntity.notFound().build());
    }
}
