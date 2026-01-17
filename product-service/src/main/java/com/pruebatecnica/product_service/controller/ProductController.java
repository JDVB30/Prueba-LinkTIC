package com.pruebatecnica.product_service.controller;

import com.pruebatecnica.product_service.dto.JsonApiDocument;
import com.pruebatecnica.product_service.model.Product;
import com.pruebatecnica.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository repository;

    @GetMapping
    public List<JsonApiDocument<Product>> findAll(Pageable pageable) {
        Page<Product> products = repository.findAll(pageable);
        return products.stream()
                .map(p -> JsonApiDocument.of("products", p.getId().toString(), p))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public JsonApiDocument<Product> findById(@PathVariable Long id) {
        Product p = repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return JsonApiDocument.of("products", p.getId().toString(), p);
    }

    @PostMapping
    public JsonApiDocument<Product> create(@RequestBody Product product) {
        Product saved = repository.save(product);
        return JsonApiDocument.of("products", saved.getId().toString(), saved);
    }

    @PutMapping("/{id}")
    public JsonApiDocument<Product> update(@PathVariable Long id, @RequestBody Product productDetails) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());

        Product updated = repository.save(product);
        return JsonApiDocument.of("products", updated.getId().toString(), updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        repository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
