package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.infrastructure.adapter.in.response.ProcessamentoResponse;

import java.util.List;

public interface BuscarStatusProcessamentoUseCase {
    List<ProcessamentoResponse> execute(String idUsuario);
}
