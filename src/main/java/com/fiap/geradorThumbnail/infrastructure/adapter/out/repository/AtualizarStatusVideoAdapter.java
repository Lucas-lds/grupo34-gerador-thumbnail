package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository;

import com.fiap.geradorThumbnail.application.port.out.AtualizarStatusVideo;
import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamentoEntity;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories.ProcessamentoRepository;
import org.springframework.stereotype.Component;

@Component
public class AtualizarStatusVideoAdapter implements AtualizarStatusVideo {
    private final ProcessamentoRepository repository;

    public AtualizarStatusVideoAdapter(ProcessamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(StatusProcessamento status, Long idProcessamento) {
        var processamentoEntityOpt = repository.findById(idProcessamento);
        var processamentoEntity = processamentoEntityOpt.get();
        processamentoEntity.setStatus(StatusProcessamentoEntity.toEntity(status));
        repository.save(processamentoEntity);

    }
}
