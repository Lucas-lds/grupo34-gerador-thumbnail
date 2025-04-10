package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import com.fiap.geradorThumbnail.application.port.out.VideoProcessorServicePortOut;
import com.fiap.geradorThumbnail.application.service.SalvarVideoService;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.infrastructure.utils.ZipUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ContentDisposition;
import org.springframework.core.io.InputStreamResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class VideoProcessingController {

    @Autowired
    private VideoProcessorServicePortOut videoProcessor;
    
    @Autowired
    private SalvarVideoService salvarVideoService;

    @Operation(summary = "Processar vídeo e extrair frames", description = "Carregue vários arquivos de vídeo para extrair frames e obter um ZIP com todos os frames")
    @PostMapping(value = "/video/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processVideos(@RequestPart(value = "files", required = true) @Parameter(description = "Vídeo para processar", 
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            schema = @Schema(type = "string", format = "binary")))
            List<MultipartFile> videoFiles,
            @RequestPart(value = "userId", required = true) String userId) {

        try {
            // Diretório temporário para processamento
            String baseOutputFolder = System.getProperty("java.io.tmpdir") + File.separator + "gerador-thumbnail";
            Path tempDir = Path.of(baseOutputFolder, "temp");
            Files.createDirectories(tempDir);
            new File(baseOutputFolder).mkdirs();

            // Processa cada vídeo em paralelo
            List<CompletableFuture<String>> futures = videoFiles.stream()
                .map(videoFile -> {
                    String videoId = UUID.randomUUID().toString();
                    String outputFolder = baseOutputFolder + "/" + videoId;
                    new File(outputFolder).mkdirs();
                    
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            // Save uploaded file to temp location
                            // Get original filename safely
                            String filename = videoFile.getOriginalFilename();
                            if (filename == null || filename.isEmpty()) {
                                filename = "video_" + UUID.randomUUID();
                            }
                            
                            Path tempFile = tempDir.resolve(filename);
                            try (InputStream inputStream = videoFile.getInputStream()) {
                                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                            }
                            
                            // Process the saved file with absolute path
                            int frameCount = videoProcessor.processVideo(tempFile.toAbsolutePath().toString(), outputFolder);
                            return outputFolder + "|" + frameCount + "|" + tempFile.toAbsolutePath().toString();
                        } catch (IOException e) {
                            throw new RuntimeException("Erro ao processar vídeo: " + videoFile.getOriginalFilename(), e);
                        }
                    });
                })
                .collect(Collectors.toList());

            // Aguarda conclusão de todos os processamentos
            List<String> outputFolders = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()))
                .get();

            // Gera arquivo zip com todos os frames como stream
            List<String> folders = outputFolders.stream().map(s -> s.split("\\|")[0]).collect(Collectors.toList());
            InputStreamResource zipStream = ZipUtil.createZipAsStream(baseOutputFolder, folders);

            // Configura resposta para download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment()
                .filename("frames.zip")
                .build());

            // Para cada vídeo processado, persiste as informações
            for (String output : outputFolders) {
                String[] parts = output.split("\\|");
                String outputFolder = parts[0];
                int frameCount = Integer.parseInt(parts[1]);
                String videoPath = parts[2];
                
                String nomeArquivo = videoPath.substring(videoPath.lastIndexOf(File.separator) + 1);
                // Read video file bytes
                byte[] videoBytes = Files.readAllBytes(Path.of(videoPath));
                
                Video video = new Video(
                    videoBytes,
                    userId,
                    nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1), // formatoVideo
                    nomeArquivo,
                    outputFolder,  // caminhoFrames
                    videoPath,     // caminhoVideoOriginal
                    null,          // caminhoZip (não usado mais)
                    frameCount     // quantidadeFrames
                );
                salvarVideoService.executar(video);
            }

            // Adiciona informações no cabeçalho e retorna o arquivo para download
            headers.add("X-Message", "Vídeos processados com sucesso!");
            headers.add("X-Output-Path", new File(baseOutputFolder).getAbsolutePath());
            return ResponseEntity.ok()
                .headers(headers)
                .body(zipStream);
        } catch (IOException | InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).body("Erro ao processar vídeos: " + e.getMessage());
        }
    }
}
