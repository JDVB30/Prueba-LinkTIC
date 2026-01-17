package com.pruebatecnica.inventory_service.service;

import com.pruebatecnica.inventory_service.client.ProductClient;
import com.pruebatecnica.inventory_service.model.Inventory;
import com.pruebatecnica.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository repository;
    private final ProductClient productClient;

    public Inventory updateStock(Long productId, Integer quantity) {
        // 1. Validar con Resiliencia
        productClient.existsProduct(productId);

        // 2. Lógica de negocio
        Inventory inventory = repository.findByProductId(productId)
                .orElse(Inventory.builder().productId(productId).stock(0).build());

        inventory.setStock(inventory.getStock() + quantity);
        Inventory saved = repository.save(inventory);

        // 3. Log Estructurado
        log.info("{\"event\": \"STOCK_CHANGE\", \"productId\": {}, \"newQuantity\": {}}", productId, saved.getStock());

        return saved;
    }

    public Inventory getInventoryByProductId(Long productId) {
        // Llama al microservicio de productos (esto cumple el requisito)
        productClient.existsProduct(productId);

        return repository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para este producto"));
    }
}
