package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarProcessamento;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.entity.VideoEntity;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamento;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ArmazenarProcessamentoAdapter implements ArmazenarProcessamento {

    private final VideoRepository repository;

    public ArmazenarProcessamentoAdapter(VideoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Video video) {
        var videoEntity = new VideoEntity(
            UUID.randomUUID(),
            video.getIdUsuario(),
            video.getNomeArquivo(),
            video.getCaminhoFrames(),
            video.getCaminhoVideoOriginal(),
            video.getCaminhoZip(),
            video.getQuantidadeFrames(),
            StatusProcessamento.PROCESSANDO
        );
        repository.save(videoEntity);
        System.out.println("Video armazenado no banco de dados com sucesso!");
    }
}
