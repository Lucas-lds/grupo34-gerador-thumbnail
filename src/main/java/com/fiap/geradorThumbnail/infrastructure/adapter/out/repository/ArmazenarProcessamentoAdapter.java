package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarProcessamento;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamentoEntity;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories.ProcessamentoRepository;
import org.springframework.stereotype.Component;

@Component
public class ArmazenarProcessamentoAdapter implements ArmazenarProcessamento {

    private final ProcessamentoRepository repository;

    public ArmazenarProcessamentoAdapter(ProcessamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long execute(Video video) {
        var processamento = new ProcessamentoEntity(video.getIdUsuario(), video.getNomeArquivo(), video.getStatusProcessamento().toEntity());
        var processamentoSalvo = repository.save(processamento);
        System.out.println("Processamento armazenado no banco de dados com sucesso!");
        return processamentoSalvo.getId();
    }
}
