package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus;

import java.util.List;

public interface BuscarStatusProcessamentoUseCase {
    List<ProcessamentoStatus> execute(String idUsuario);
}
