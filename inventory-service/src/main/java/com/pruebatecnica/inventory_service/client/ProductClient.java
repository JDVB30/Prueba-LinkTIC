package com.pruebatecnica.inventory_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClient {
    private final RestTemplate restTemplate;
    private final String PRODUCT_SERVICE_URL = "http://product-service:8081/api/v1/products/";

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackValidateProduct")
    public boolean existsProduct(Long productId) {
        log.info("Validando existencia del producto ID: {}", productId);
        restTemplate.getForObject(PRODUCT_SERVICE_URL + productId, Object.class);
        return true;
    }

    public boolean fallbackValidateProduct(Long productId, Throwable t) {
        log.error("CIRCUIT BREAKER: El servicio de productos falló. Detalle: {}", t.getMessage());
        throw new RuntimeException("El servicio de productos no está disponible temporalmente.");
    }
}
