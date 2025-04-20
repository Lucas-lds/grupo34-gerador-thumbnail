package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarThumbnails;
import com.fiap.geradorThumbnail.application.port.out.BuscarVideo;
import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;
import org.bytedeco.javacv.Frame;
import java.awt.image.BufferedImage;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("BDD Tests for GerarThumbnailAdapterOut")
public class GerarThumbnailAdapterOutBDDTest {

    private BuscarVideo buscarVideo;
    private ArmazenarThumbnails armazenarThumbnails;
    private GerarThumbnailAdapterOut gerarThumbnailAdapterOut;

    @BeforeEach
    void setup() {
        buscarVideo = mock(BuscarVideo.class);
        armazenarThumbnails = mock(ArmazenarThumbnails.class);
        gerarThumbnailAdapterOut = new GerarThumbnailAdapterOut(buscarVideo, armazenarThumbnails);
    }

    @Nested
    @DisplayName("execute method tests")
    class ExecuteMethodTests {

        @Test
        @DisplayName("Should generate and store thumbnails for video frames at intervals")
        void shouldGenerateAndStoreThumbnails() throws Exception {
            // Caminho real do arquivo de vídeo na pasta resources
            String realPath = "src/test/resources/test_video.mp4";
            File videoFile = new File(realPath);
            InputStream videoStream = new FileInputStream(videoFile);

            // Nome simulado com estrutura correta (usuário + nome base + UUID)
            String videoName = "videos/usuario123/video_test_" + UUID.randomUUID() + ".mp4";

            // Mock do ProcessamentoVideo
            ProcessamentoVideo processamentoVideo = mock(ProcessamentoVideo.class);
            when(processamentoVideo.nomeVideo()).thenReturn(videoName);
            when(buscarVideo.execute(videoName)).thenReturn(videoStream);

            try (var mockedGrabber = mockConstruction(FFmpegFrameGrabber.class,
                        (mock, context) -> {
                            when(mock.getFrameRate()).thenReturn(30.0);
                            when(mock.getLengthInFrames()).thenReturn(3);
                            when(mock.grabImage())
                                .thenReturn(new Frame())
                                .thenReturn(new Frame())
                                .thenReturn(null);
                            doNothing().when(mock).start();
                            doNothing().when(mock).stop();
                        });
                var mockedConverter = mockConstruction(Java2DFrameConverter.class,
                        (mock, context) -> {
                            BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                            when(mock.convert(any(Frame.class))).thenReturn(dummyImage);
                        })) {

                // Execução do método
                assertThatCode(() -> gerarThumbnailAdapterOut.execute(processamentoVideo))
                        .doesNotThrowAnyException();

                // Verificações
                ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
                ArgumentCaptor<byte[]> bytesCaptor = ArgumentCaptor.forClass(byte[].class);

                verify(armazenarThumbnails, atLeastOnce()).execute(pathCaptor.capture(), bytesCaptor.capture());

                // Assertivas nos resultados
                for (String path : pathCaptor.getAllValues()) {
                    assertThat(path).isNotBlank();
                }
                for (byte[] bytes : bytesCaptor.getAllValues()) {
                    assertThat(bytes).isNotNull();
                }
            }
        }


        @Test
        @DisplayName("Should throw RuntimeException when IOException occurs")
        void shouldThrowRuntimeExceptionOnIOException() throws Exception {
            String videoName = "C:/Users/lucas/OneDrive/Documentos/grupo34-gerador-thumbnail/src/test/resources/test-video.mp4";
            ProcessamentoVideo processamentoVideo = mock(ProcessamentoVideo.class);
            when(processamentoVideo.nomeVideo()).thenReturn(videoName);

            InputStream videoStream = mock(InputStream.class);
            when(buscarVideo.execute(videoName)).thenReturn(videoStream);
            when(videoStream.readAllBytes()).thenThrow(new java.io.IOException("IO error"));

            assertThatThrownBy(() -> gerarThumbnailAdapterOut.execute(processamentoVideo))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Erro ao gerar thumbnails do vídeo");
        }
    }
}
