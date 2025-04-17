package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarProcessamento;
import com.fiap.geradorThumbnail.application.port.out.ArmazenarVideo;
import com.fiap.geradorThumbnail.application.port.out.EnviarNotificacaoVideo;
import com.fiap.geradorThumbnail.core.domain.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SalvarVideoServiceTest {

    private ArmazenarVideo armazenarVideo;
    private EnviarNotificacaoVideo enviarNotificacaoVideo;
    private ArmazenarProcessamento armazenarProcessamento;
    private SalvarVideoService salvarVideoService;

    @BeforeEach
    void setUp() {
        armazenarVideo = Mockito.mock(ArmazenarVideo.class);
        enviarNotificacaoVideo = Mockito.mock(EnviarNotificacaoVideo.class);
        armazenarProcessamento = Mockito.mock(ArmazenarProcessamento.class);
        salvarVideoService = new SalvarVideoService(armazenarVideo, enviarNotificacaoVideo, armazenarProcessamento);
    }

    @Test
    void givenValidVideo_whenExecutar_thenDependenciesAreCalled() {
        // Given
        Video video = Mockito.mock(Video.class);

        // When
        salvarVideoService.executar(video);

        // Then
        verify(armazenarVideo, times(1)).execute(video);
        verify(armazenarProcessamento, times(1)).execute(video);
        verify(enviarNotificacaoVideo, times(1)).execute(video);
    }
}
