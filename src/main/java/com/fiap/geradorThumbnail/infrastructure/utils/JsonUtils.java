package com.fiap.geradorThumbnail.infrastructure.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {

    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> tipo) {
        try {
            return OBJECT_MAPPER.readValue(json, tipo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter JSON para objeto do tipo " + tipo.getSimpleName(), e);
        }
    }


}
