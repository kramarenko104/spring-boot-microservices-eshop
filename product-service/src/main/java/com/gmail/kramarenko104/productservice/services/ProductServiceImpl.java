package com.gmail.kramarenko104.productservice.services;

import com.gmail.kramarenko104.productservice.model.Product;
import com.gmail.kramarenko104.productservice.repositories.ProductCrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductCrudRepo productRepo;

    @Autowired
    public ProductServiceImpl(ProductCrudRepo productRepo){
        this.productRepo = productRepo;
    }

    @Override
    public Product create(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product get(long productId) {
        return (productRepo.findById(productId).isPresent() ? productRepo.findById(productId).get() : null);
    }

    @Override
    public List<Product> getAll() {
        return (List)(productRepo.findAll());
    }

    @Override
    public List<Product> getProductsByCategory(int category) {
        return productRepo.findByCategory(category);
    }


    @Override
    public void delete(long productId) {
        productRepo.deleteById(productId);
    }

}
