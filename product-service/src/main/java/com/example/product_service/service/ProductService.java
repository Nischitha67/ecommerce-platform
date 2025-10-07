package com.example.product_service.service;

import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product updateProduct(Long id, Product product) {
    	return repository.findById(id)
    	        .map(existing -> {
    	            existing.setName(product.getName());
    	            existing.setDescription(product.getDescription());
    	            existing.setPrice(product.getPrice());
    	            existing.setQuantity(product.getQuantity()); // updated field
    	            return repository.save(existing);
    	        })
    	        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

    }

    public boolean deleteProduct(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true; // successfully deleted
        } else {
            return false; // product not found
        }
    }

}
