package com.example.product_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.client.AuthClient;
import com.example.product_service.model.Product;
import com.example.product_service.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService service;
    private final AuthClient authClient; // Feign or REST client to Auth Service
    public ProductController(ProductService service, AuthClient authClient) {
        this.service = service;
        this.authClient = authClient;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product,
                                    @RequestHeader("Authorization") String tokenHeader) {
        if (!authClient.isAdmin(tokenHeader)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        return ResponseEntity.ok(service.createProduct(product));
    }

    @GetMapping
    public List<Product> getAll() {

        log.debug("get all");
        return service.getAllProducts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Product product,
                                    @RequestHeader("Authorization") String tokenHeader) {
        if (!authClient.isAdmin(tokenHeader)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        return ResponseEntity.ok(service.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("Authorization") String tokenHeader) {
        if (!authClient.isAdmin(tokenHeader)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        service.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
