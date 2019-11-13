package com.gmail.kramarenko104.productservice.controllers;

import com.gmail.kramarenko104.productservice.model.Product;
import com.gmail.kramarenko104.productservice.services.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private static Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    ProductServiceImpl productService;

    public ProductRestController() {
    }

    @PostMapping()
    public HttpEntity<Product> createProduct(@RequestParam ("product") Product product) {
        return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public HttpEntity<Product> getProduct(@PathVariable("productId") long productId) {
        return new ResponseEntity<>(productService.get(productId),  HttpStatus.OK);
    }

    @GetMapping
    public HttpEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping(params = "category")
    public HttpEntity<List<Product>> getProductsByCategory(@RequestParam int category) {
        return new ResponseEntity<>(productService.getProductsByCategory(category), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable("productId") long productId) {
        productService.delete(productId);
    }

}
