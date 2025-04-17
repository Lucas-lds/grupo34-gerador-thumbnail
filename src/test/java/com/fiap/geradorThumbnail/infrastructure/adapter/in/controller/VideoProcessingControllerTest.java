package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import com.fiap.geradorThumbnail.application.port.out.VideoProcessorServicePortOut;
import com.fiap.geradorThumbnail.application.service.SalvarVideoService;
import com.fiap.geradorThumbnail.infrastructure.utils.ZipUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VideoProcessingControllerTest {

    private VideoProcessorServicePortOut videoProcessor;
    private SalvarVideoService salvarVideoService;
    private VideoProcessingController controller;

    @BeforeEach
    void setUp() throws Exception {
        videoProcessor = mock(VideoProcessorServicePortOut.class);
        salvarVideoService = mock(SalvarVideoService.class);
        controller = new VideoProcessingController();

        // Use reflection to set private fields
        java.lang.reflect.Field videoProcessorField = VideoProcessingController.class.getDeclaredField("videoProcessor");
        videoProcessorField.setAccessible(true);
        videoProcessorField.set(controller, videoProcessor);

        java.lang.reflect.Field salvarVideoServiceField = VideoProcessingController.class.getDeclaredField("salvarVideoService");
        salvarVideoServiceField.setAccessible(true);
        salvarVideoServiceField.set(controller, salvarVideoService);
    }

    @Test
    void givenValidVideos_whenProcessVideos_thenReturnsOkWithZipStream() throws Exception {
        // Prepare mock video files
        MockMultipartFile video1 = new MockMultipartFile("files", "video1.mp4", "video/mp4", "dummy content 1".getBytes());
        MockMultipartFile video2 = new MockMultipartFile("files", "video2.mp4", "video/mp4", "dummy content 2".getBytes());
        List<MultipartFile> videos = List.of(video1, video2);

        // Mock videoProcessor to return frame count 10 for any video
        when(videoProcessor.processVideo(anyString(), anyString())).thenReturn(10);

        // Mock ZipUtil to return a dummy InputStreamResource
        // Since ZipUtil.createZipAsStream is static, we cannot mock easily without PowerMockito
        // So we rely on actual method or skip mocking it here

        // Call controller method
        ResponseEntity<?> response = controller.processVideos(videos, "user123");

        // Verify response status and headers
        assertEquals(200, response.getStatusCodeValue());
        HttpHeaders headers = response.getHeaders();
        assertTrue(headers.getContentType().toString().contains("application/octet-stream"));
        assertEquals("attachment; filename=\"frames.zip\"", headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("Vídeos processados com sucesso!", headers.getFirst("X-Message"));
        assertNotNull(headers.getFirst("X-Output-Path"));

        // Verify body is InputStreamResource
        assertTrue(response.getBody() instanceof InputStreamResource);

        // Verify salvarVideoService.executar called twice (once per video)
        verify(salvarVideoService, times(2)).executar(any());

        // Verify videoProcessor.processVideo called twice
        verify(videoProcessor, times(2)).processVideo(anyString(), anyString());
    }

    @Test
    void givenIOException_whenProcessVideos_thenReturnsServerError() throws Exception {
        MockMultipartFile video = new MockMultipartFile("files", "video.mp4", "video/mp4", "dummy content".getBytes());
        List<MultipartFile> videos = List.of(video);

        // Mock videoProcessor to throw IOException wrapped in RuntimeException
        when(videoProcessor.processVideo(anyString(), anyString())).thenThrow(new RuntimeException(new IOException("IO error")));

        ResponseEntity<?> response = controller.processVideos(videos, "user123");

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erro ao processar vídeos"));
    }
}
