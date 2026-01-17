package com.pruebatecnica.inventory_service.controller;

import com.pruebatecnica.inventory_service.dto.JsonApiDocument;
import com.pruebatecnica.inventory_service.model.Inventory;
import com.pruebatecnica.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    // EL ERROR SUELE ESTAR AQUÍ: Asegúrate de tener el /{productId}
    @PostMapping("/{productId}")
    public ResponseEntity<JsonApiDocument<Inventory>> updateStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        Inventory updated = service.updateStock(productId, quantity);

        return ResponseEntity.ok(JsonApiDocument.of(
                "inventory",
                updated.getId().toString(),
                updated
        ));
    }
    @GetMapping("/{productId}")
    public JsonApiDocument<Inventory> getStock(@PathVariable Long productId) {
        // 1. El service debe llamar al ProductClient para validar que el producto existe
        // 2. Luego busca el inventario
        Inventory inventory = service.getInventoryByProductId(productId);

        return JsonApiDocument.of("inventory", inventory.getId().toString(), inventory);
    }
}
