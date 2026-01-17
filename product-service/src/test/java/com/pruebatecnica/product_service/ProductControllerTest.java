package com.pruebatecnica.product_service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProduct_Success() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .header("X-API-KEY", "SECRET123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\", \"price\":10.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.name").value("Test"));
    }

    @Test
    void getProduct_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/products/999")
                        .header("X-API-KEY", "SECRET123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].title").value("Not Found"));
    }
}
