package com.gmail.kramarenko104.productservice.services;

import com.gmail.kramarenko104.productservice.model.Product;
import java.util.List;

public interface ProductService {

    Product create(Product product);

    Product get(long productId);

    List<Product> getAll();

    List<Product> getProductsByCategory(int category);

    void delete(long productId);

}
