package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.out.VideoProcessorServicePortOut;
import com.fiap.geradorThumbnail.application.port.out.VideoProcessorAdapterPortOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class VideoProcessingService implements VideoProcessorServicePortOut {

    private final VideoProcessorAdapterPortOut videoProcessorAdapter;

    @Autowired
    public VideoProcessingService(VideoProcessorAdapterPortOut videoProcessorAdapter) {
        this.videoProcessorAdapter = videoProcessorAdapter;
    }

    @Override
    public int processVideo(String videoPath, String outputFolder) throws IOException {
        return videoProcessorAdapter.extractFrames(videoPath, outputFolder);
    }
}