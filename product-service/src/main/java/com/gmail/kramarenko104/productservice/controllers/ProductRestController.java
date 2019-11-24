package com.gmail.kramarenko104.productservice.controllers;

import com.gmail.kramarenko104.productservice.model.Product;
import com.gmail.kramarenko104.productservice.services.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private static Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    private Environment env;

    public ProductRestController() {
    }

    @PostMapping()
    public HttpEntity<String> createProduct(@RequestParam ("product") Product product) {
        return new ResponseEntity<>(productService.create(product).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public HttpEntity<String> getProduct(@PathVariable("productId") long productId) {
        return new ResponseEntity<>(productService.get(productId).toString(),  HttpStatus.OK);
    }

    @GetMapping("/api/{productId}")
    public Product getProductAPI(@PathVariable("productId") long productId) {
        logger.debug("[eshop] Product-Service running at port: " + env.getProperty("local.server.port"));
        return productService.get(productId);
    }

    @GetMapping
    public HttpEntity<List<String>> getAllProducts() {
        List<String> products =
                productService.getAll()
                .stream()
                .map(product -> product.toString())
                .collect(Collectors.toList());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(params = "category")
    public HttpEntity<List<String>> getProductsByCategory(@RequestParam int category) {
        List<String> products =
                productService.getProductsByCategory(category)
                        .stream()
                        .map(product -> product.toString())
                        .collect(Collectors.toList());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public HttpStatus deleteProduct(@PathVariable("productId") long productId) {
        productService.delete(productId);
        return HttpStatus.OK;
    }

}
