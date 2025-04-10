package com.fiap.geradorThumbnail.application.port.out;

import java.io.IOException;

public interface VideoProcessorAdapterPortOut {
    int extractFrames(String videoPath, String outputFolder) throws IOException;
}
