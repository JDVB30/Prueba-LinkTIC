package com.pruebatecnica.inventory_service;

import com.pruebatecnica.inventory_service.client.ProductClient;
import com.pruebatecnica.inventory_service.model.Inventory;
import com.pruebatecnica.inventory_service.repository.InventoryRepository;
import com.pruebatecnica.inventory_service.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = Inventory.builder()
                .productId(1L)
                .stock(10)
                .build();
    }

    @Test
    void whenUpdateStock_thenVerifyFlowAndSave() {
        // Arrange
        when(productClient.existsProduct(1L)).thenReturn(true);
        when(repository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(repository.save(any(Inventory.class))).thenReturn(inventory);

        // Act
        Inventory result = inventoryService.updateStock(1L, 5);

        // Assert
        assertNotNull(result);
        assertEquals(15, result.getStock());
        verify(productClient, times(1)).existsProduct(1L);
        verify(repository, times(1)).save(any(Inventory.class));
    }

    @Test
    void whenProductServiceFails_thenThrowException() {
        // Arrange
        when(productClient.existsProduct(1L)).thenThrow(new RuntimeException("Service Unavailable"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> inventoryService.updateStock(1L, 5));
        verify(repository, never()).save(any());
    }
}
