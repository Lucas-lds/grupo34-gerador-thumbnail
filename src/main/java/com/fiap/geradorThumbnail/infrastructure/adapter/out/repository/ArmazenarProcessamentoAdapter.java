package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarProcessamento;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories.ProcessamentoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArmazenarProcessamentoAdapter implements ArmazenarProcessamento {

    private final ProcessamentoRepository repository;

    public ArmazenarProcessamentoAdapter(ProcessamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Long> execute(List<Video> videos) {
        List<Long> idsProcessamentos = new ArrayList<>();

        for (Video video : videos) {
            var processamento = new ProcessamentoEntity(
                    video.getIdUsuario(),
                    video.getCaminhoVideo(),
                    video.getStatusProcessamento().toEntity()
            );

            var processamentoSalvo = repository.save(processamento);
            System.out.println("✅ Processamento armazenado com sucesso: " + processamentoSalvo.getId());
            idsProcessamentos.add(processamentoSalvo.getId());
        }

        return idsProcessamentos;
    }
}
