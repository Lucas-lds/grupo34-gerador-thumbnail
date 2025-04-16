package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarThumbnails;
import com.fiap.geradorThumbnail.application.port.out.BuscarVideo;
import com.fiap.geradorThumbnail.application.port.out.GerarThumbnail;
import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;
import com.fiap.geradorThumbnail.infrastructure.utils.ThumbnailUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class GerarThumbnailAdapterOut implements GerarThumbnail {

    private final BuscarVideo buscarVideo;
    private final ArmazenarThumbnails armazenarThumbnails;

    public GerarThumbnailAdapterOut(BuscarVideo buscarVideo, ArmazenarThumbnails armazenarThumbnails) {
        this.buscarVideo = buscarVideo;
        this.armazenarThumbnails = armazenarThumbnails;
    }

    @Override
    public void execute(ProcessamentoVideo processamentoVideo) {
        String caminhoVideo = processamentoVideo.nomeVideo();

        try (InputStream videoStream = buscarVideo.execute(caminhoVideo)) {
            byte[] videoBytes = videoStream.readAllBytes();

            try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new ByteArrayInputStream(videoBytes))) {
                grabber.start();
                double frameRate = grabber.getFrameRate();
                int interval = (int) (frameRate * 20);

                Java2DFrameConverter converter = new Java2DFrameConverter();
                int frameNumber = 0;

                for (int i = 0; i < grabber.getLengthInFrames(); i++) {
                    Frame frame = grabber.grabImage();
                    if (frame != null && i % interval == 0) {
                        BufferedImage image = converter.convert(frame);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpg", baos);
                        byte[] imagemBytes = baos.toByteArray();

                        String caminho = ThumbnailUtils.gerarCaminhoThumbnail(caminhoVideo) + "_frame_" + frameNumber + ".jpg";
                        armazenarThumbnails.execute(caminho, imagemBytes);
                        frameNumber++;
                    }
                }

                grabber.stop();

            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar thumbnails do vídeo: " + caminhoVideo, e);
        }
    }
}
