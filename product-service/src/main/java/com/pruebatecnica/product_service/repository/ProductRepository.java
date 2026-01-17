package com.pruebatecnica.product_service.repository;

import com.pruebatecnica.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Product.
 * Hereda de JpaRepository para obtener soporte de Paginación (PagingAndSortingRepository).
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Al extender JpaRepository, ya tenemos:
    // - save(Product)
    // - findById(Long)
    // - findAll(Pageable) -> Crucial para el requerimiento de paginación simple.
    // - deleteById(Long)
}
