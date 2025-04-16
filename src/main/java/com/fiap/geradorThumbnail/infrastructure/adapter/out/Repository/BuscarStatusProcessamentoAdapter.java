package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository;

import com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus;
import com.fiap.geradorThumbnail.core.usecases.BuscarStatusProcessamentoUseCase;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.response.ProcessamentoResponse;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories.ProcessamentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuscarStatusProcessamentoAdapter implements BuscarStatusProcessamentoUseCase {

    private final ProcessamentoRepository repository;

    public BuscarStatusProcessamentoAdapter(ProcessamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProcessamentoStatus> execute(String idUsuario) {
        return repository.findByIdClienteOrderByStatusAscCriadoEmDesc(idUsuario).stream()
                .map(p -> new ProcessamentoStatus(
                        p.getId(),
                        p.getNomeVideo(),
                        p.getStatus().name(),
                        p.getCriadoEm()
                ))
                .toList();
    }
}
