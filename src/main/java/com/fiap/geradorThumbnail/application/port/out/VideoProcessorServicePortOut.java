package com.fiap.geradorThumbnail.application.port.out;

import java.io.IOException;

public interface VideoProcessorServicePortOut {
    int processVideo(String videoPath, String outputFolder) throws IOException;
}
