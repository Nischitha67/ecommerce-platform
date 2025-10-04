package com.example.product_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.product_service.model.Product;
import com.example.product_service.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // All POST/PUT/DELETE are now protected by API Gateway
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(service.createProduct(product));
    }

    @GetMapping
    public List<Product> getAll() {
        log.debug("Fetching all products");
        return service.getAllProducts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(service.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
