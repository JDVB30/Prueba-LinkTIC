package com.pruebatecnica.inventory_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import com.pruebatecnica.inventory_service.service.InventoryService;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureMockRestServiceServer
class InventoryIntegrationTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void testFullFlowWithMockedExternalService() {
        // Simular respuesta exitosa del Product Service (Puerto 8081)
        this.mockServer.expect(requestTo("http://product-service:8081/api/v1/products/1"))
                .andRespond(withSuccess("{\"data\": {\"id\": \"1\"}}", MediaType.APPLICATION_JSON));

        // Ejecutar lógica que dispara la llamada HTTP y el Circuit Breaker
        assertDoesNotThrow(() -> inventoryService.updateStock(1L, 10));

        this.mockServer.verify();
    }
}
