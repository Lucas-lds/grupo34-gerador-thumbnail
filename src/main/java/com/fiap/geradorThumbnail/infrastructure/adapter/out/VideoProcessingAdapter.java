package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.application.port.out.VideoProcessorAdapterPortOut;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class VideoProcessingAdapter implements VideoProcessorAdapterPortOut {

    @Override
    public int extractFrames(String videoPath, String outputFolder) throws IOException {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();
            
            double frameRate = grabber.getFrameRate();
            int interval = (int) (frameRate * 20);
            
            int frameNumber = 0;
            try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                for (int i = 0; i < grabber.getLengthInFrames(); i++) {
                    Frame frame = grabber.grabImage();
                    
                    if (frame != null && i % interval == 0) {
                        BufferedImage bufferedImage = converter.convert(frame);
                        File outputFile = new File(outputFolder, "frame_at_" + frameNumber + ".jpg");
                        ImageIO.write(bufferedImage, "jpg", outputFile);
                        frameNumber++;
                    }
                }
            }
            grabber.stop();
            return frameNumber;
        }
    }
}
