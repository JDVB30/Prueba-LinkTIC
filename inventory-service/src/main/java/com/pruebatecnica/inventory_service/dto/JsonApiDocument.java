package com.pruebatecnica.inventory_service.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiDocument<T> {

    private DataContainer<T> data;
    private List<Map<String, Object>> errors; // Para soportar el formato de errores

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContainer<T> {
        private String id;
        private String type;
        private T attributes;
    }

    /**
     * Método utilitario estático para crear respuestas exitosas rápidamente.
     * @param type El tipo de recurso (ej. "products", "inventory")
     * @param id El ID del recurso en formato String
     * @param attributes El objeto (Entidad o DTO) con los datos
     */
    public static <T> JsonApiDocument<T> of(String type, String id, T attributes) {
        DataContainer<T> container = DataContainer.<T>builder()
                .id(id)
                .type(type)
                .attributes(attributes)
                .build();

        return JsonApiDocument.<T>builder()
                .data(container)
                .build();
    }
}
