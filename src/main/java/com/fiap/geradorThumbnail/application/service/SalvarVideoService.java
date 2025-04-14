package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarProcessamento;
import com.fiap.geradorThumbnail.core.usecases.SalvarVideoUseCase;
import com.fiap.geradorThumbnail.application.port.out.ArmazenarVideo;
import com.fiap.geradorThumbnail.application.port.out.EnviarNotificacaoVideo;
import com.fiap.geradorThumbnail.core.domain.Video;

public class SalvarVideoService implements SalvarVideoUseCase {

    private final ArmazenarVideo armazenarVideo;
    private final EnviarNotificacaoVideo enviarNotificacaoVideo;
    private final ArmazenarProcessamento armazenarProcessamento;

    public SalvarVideoService(ArmazenarVideo armazenarVideo, EnviarNotificacaoVideo enviarNotificacaoVideo,
                              ArmazenarProcessamento armazenarProcessamento) {
        this.armazenarVideo = armazenarVideo;
        this.enviarNotificacaoVideo = enviarNotificacaoVideo;
        this.armazenarProcessamento = armazenarProcessamento;
    }

    @Override
    public void executar(Video video) {
        armazenarVideo.execute(video);
        var idProcessamento = armazenarProcessamento.execute(video);
        enviarNotificacaoVideo.execute(video, idProcessamento);
    }
}
