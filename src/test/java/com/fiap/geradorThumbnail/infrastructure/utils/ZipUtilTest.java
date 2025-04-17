package com.fiap.geradorThumbnail.infrastructure.utils;

import org.junit.jupiter.api.*;
import org.springframework.core.io.InputStreamResource;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("ziputiltest");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testCreateZip() throws IOException {
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Files.writeString(file1, "Hello World");

        String zipPath = ZipUtil.createZip(tempDir.toString());

        assertTrue(Files.exists(Path.of(zipPath)));
        assertTrue(zipPath.endsWith(".zip"));

        // Check zip content
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry);
            assertEquals("file1.txt", entry.getName());
        }
    }

    @Test
    void testCreateZipAsStream() throws IOException {
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Files.writeString(file1, "Hello Stream");

        InputStreamResource resource = ZipUtil.createZipAsStream(tempDir.toString(), List.of(tempDir.toString()));

        assertNotNull(resource);
        try (ZipInputStream zis = new ZipInputStream(resource.getInputStream())) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry);
            assertEquals("file1.txt", entry.getName());
        }
    }

    @Test
    void testCreateZipFromMultipleFolders() throws IOException {
        Path folder1 = Files.createDirectory(tempDir.resolve("folder1"));
        Path folder2 = Files.createDirectory(tempDir.resolve("folder2"));

        Path file1 = Files.createFile(folder1.resolve("file1.txt"));
        Path file2 = Files.createFile(folder2.resolve("file2.txt"));

        Files.writeString(file1, "File 1 content");
        Files.writeString(file2, "File 2 content");

        String zipPath = ZipUtil.createZipFromMultipleFolders(tempDir.toString(), List.of(folder1.toString(), folder2.toString()));

        assertTrue(Files.exists(Path.of(zipPath)));
        assertTrue(zipPath.endsWith(".zip"));

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            boolean foundFile1 = false;
            boolean foundFile2 = false;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith("file1.txt")) {
                    foundFile1 = true;
                }
                if (entry.getName().endsWith("file2.txt")) {
                    foundFile2 = true;
                }
            }
            assertTrue(foundFile1);
            assertTrue(foundFile2);
        }
    }
}
