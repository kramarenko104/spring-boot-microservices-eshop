package com.gmail.kramarenko104.productservice.repositories;

import com.gmail.kramarenko104.productservice.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductCrudRepo extends CrudRepository<Product, Long> {

    List<Product> findByCategory(int category);
}
