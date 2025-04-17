package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.out.VideoProcessorAdapterPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VideoProcessingServiceTest {

    private VideoProcessorAdapterPortOut videoProcessorAdapterPortOut;
    private VideoProcessingService videoProcessingService;

    @BeforeEach
    void setUp() {
        videoProcessorAdapterPortOut = Mockito.mock(VideoProcessorAdapterPortOut.class);
        videoProcessingService = new VideoProcessingService(videoProcessorAdapterPortOut);
    }

    @Test
    void givenVideoPathAndOutputFolder_whenProcessVideo_thenExtractFramesIsCalledAndResultReturned() throws IOException {
        // Given
        String videoPath = "video.mp4";
        String outputFolder = "output";
        int expectedFrames = 10;

        when(videoProcessorAdapterPortOut.extractFrames(videoPath, outputFolder)).thenReturn(expectedFrames);

        // When
        int actualFrames = videoProcessingService.processVideo(videoPath, outputFolder);

        // Then
        verify(videoProcessorAdapterPortOut, times(1)).extractFrames(videoPath, outputFolder);
        assertEquals(expectedFrames, actualFrames);
    }
}
