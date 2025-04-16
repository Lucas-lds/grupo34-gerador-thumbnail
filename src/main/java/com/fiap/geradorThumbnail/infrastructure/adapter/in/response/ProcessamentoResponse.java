package com.fiap.geradorThumbnail.infrastructure.adapter.in.response;

import com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus;

import java.time.LocalDateTime;

public record ProcessamentoResponse(Long id, String nomeVideo, String status, LocalDateTime criadoEm) {

    public static ProcessamentoResponse toResponse(ProcessamentoStatus processamentoStatus) {
        return new ProcessamentoResponse(processamentoStatus.id(), processamentoStatus.nomeVideo(), processamentoStatus.status(), processamentoStatus.criadoEm());
    }
}
