package com.pruebatecnica.product_service.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiDocument<T> {
    private DataContainer<T> data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContainer<T> {
        private String id;
        private String type;
        private T attributes;
    }

    // Método utilitario para construir la respuesta rápidamente
    public static <T> JsonApiDocument<T> of(String type, String id, T attributes) {
        return JsonApiDocument.<T>builder()
                .data(new DataContainer<>(id, type, attributes))
                .build();
    }
}
