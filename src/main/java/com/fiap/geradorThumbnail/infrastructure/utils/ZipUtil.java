package com.fiap.geradorThumbnail.infrastructure.utils;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.zip.*;
import org.springframework.core.io.InputStreamResource;

public class ZipUtil {

    public static String createZip(String outputFolder) throws IOException {
        return createZipFromMultipleFolders(outputFolder, List.of(outputFolder));
    }

    public static InputStreamResource createZipAsStream(String baseFolder, List<String> folderPaths) throws IOException {
        Path basePath = Paths.get(baseFolder);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String folderPath : folderPaths) {
                Path folder = Paths.get(folderPath);
                Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            addFileToZip(zos, file, basePath);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            }
        }
        return new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));
    }

    public static String createZipFromMultipleFolders(String baseFolder, List<String> folderPaths) throws IOException {
        Path basePath = Paths.get(baseFolder);
        String zipFilePath = baseFolder + ".zip";

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            for (String folderPath : folderPaths) {
                Path folder = Paths.get(folderPath);
                Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            addFileToZip(zos, file, basePath);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            }
        }

        return zipFilePath;
    }

    private static void addFileToZip(ZipOutputStream zos, Path file, Path outputFolder) throws IOException {
        try (FileInputStream fis = new FileInputStream(file.toFile())) {
            // Usa relativize corretamente entre Path
            Path relativePath = outputFolder.relativize(file);
            String zipEntryName = relativePath.toString().replace("\\", "/"); // Converte para o formato Unix

            ZipEntry zipEntry = new ZipEntry(zipEntryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }
}