package com.fiap.geradorThumbnail.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityFileTest {

    @Test
    void testValidarFormatoZipWithValidZip() {
        byte[] validZip = new byte[] {0x50, 0x4B, 0x03, 0x04};
        assertDoesNotThrow(() -> UtilityFile.validarFormatoZip(validZip));
    }

    @Test
    void testValidarFormatoZipWithInvalidZip() {
        byte[] invalidZip = new byte[] {0x00, 0x01, 0x02, 0x03};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UtilityFile.validarFormatoZip(invalidZip));
        assertEquals("❌ O arquivo enviado não está no formato .zip.", exception.getMessage());
    }

    @Test
    void testValidarFormatoZipWithNullOrShortArray() {
        assertThrows(IllegalArgumentException.class, () -> UtilityFile.validarFormatoZip(null));
        assertThrows(IllegalArgumentException.class, () -> UtilityFile.validarFormatoZip(new byte[] {0x50}));
    }

    @Test
    void testSanitizarNomeArquivo() {
        assertEquals("filename-123_.txt", UtilityFile.sanitizarNomeArquivo("file@name#-123_.txt"));
        assertEquals("", UtilityFile.sanitizarNomeArquivo(null));
        assertEquals("abcDEF123-_.", UtilityFile.sanitizarNomeArquivo("abcDEF123-_."));
        assertEquals("abcDEF123-_.", UtilityFile.sanitizarNomeArquivo("abcDEF123-_.!@#$%^&*()"));
    }

    @Test
    void testRemoverExtensaoZip() {
        assertEquals("file", UtilityFile.removerExtensaoZip("file.zip"));
        assertEquals("file.zipx", UtilityFile.removerExtensaoZip("file.zipx"));
        assertEquals("", UtilityFile.removerExtensaoZip(null));
        assertEquals("FILE", UtilityFile.removerExtensaoZip("FILE.ZIP"));
        assertEquals("file.name", UtilityFile.removerExtensaoZip("file.name.zip"));
    }
}
