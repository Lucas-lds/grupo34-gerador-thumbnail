package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.in.SalvarVideoUseCase;
import com.fiap.geradorThumbnail.application.port.out.ArmazenarVideo;
import com.fiap.geradorThumbnail.application.port.out.EnviarNotificacaoVideo;
import com.fiap.geradorThumbnail.core.domain.Video;

public class SalvarVideoService implements SalvarVideoUseCase {

    private final ArmazenarVideo armazenarVideo;
    private final EnviarNotificacaoVideo enviarNotificacaoVideo;

    public SalvarVideoService(ArmazenarVideo armazenarVideo, EnviarNotificacaoVideo enviarNotificacaoVideo) {
        this.armazenarVideo = armazenarVideo;
        this.enviarNotificacaoVideo = enviarNotificacaoVideo;
    }

    @Override
    public void executar(Video video) {
        armazenarVideo.execute(video);
        enviarNotificacaoVideo.execute("Vídeo enviado: " + video.getNome());
    }
}
