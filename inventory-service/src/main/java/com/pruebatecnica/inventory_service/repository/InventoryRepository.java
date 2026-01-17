package com.pruebatecnica.inventory_service.repository;

import com.pruebatecnica.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Busca el registro de inventario basado en el ID del microservicio de productos.
     */
    Optional<Inventory> findByProductId(Long productId);
}
