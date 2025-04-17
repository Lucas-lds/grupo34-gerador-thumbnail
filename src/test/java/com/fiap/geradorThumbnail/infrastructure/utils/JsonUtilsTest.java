package com.fiap.geradorThumbnail.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    static class TestObject {
        public String name;
        public int age;

        public TestObject() {}

        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    void testToJsonAndFromJson() {
        TestObject obj = new TestObject("John", 30);
        String json = JsonUtils.toJson(obj);
        assertNotNull(json);
        assertTrue(json.contains("John"));
        assertTrue(json.contains("30"));

        TestObject obj2 = JsonUtils.fromJson(json, TestObject.class);
        assertNotNull(obj2);
        assertEquals("John", obj2.name);
        assertEquals(30, obj2.age);
    }

    @Test
    void testToJsonWithInvalidObject() {
        Object obj = new Object() {
            // ObjectMapper may fail on this anonymous class
        };
        RuntimeException exception = assertThrows(RuntimeException.class, () -> JsonUtils.toJson(obj));
        assertTrue(exception.getMessage().contains("Erro ao converter objeto para JSON"));
    }

    @Test
    void testFromJsonWithInvalidJson() {
        String invalidJson = "{ invalid json }";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> JsonUtils.fromJson(invalidJson, TestObject.class));
        assertTrue(exception.getMessage().contains("Erro ao converter JSON para objeto"));
    }
}
