package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums;

import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;

public enum StatusProcessamentoEntity {
    AGUARDANDO,
    PROCESSANDO,
    FINALIZADO,
    ERRO;

    public StatusProcessamento toDomain() {
        return StatusProcessamento.valueOf(this.name());
    }

    public static StatusProcessamentoEntity toEntity(StatusProcessamento status) {
        return StatusProcessamentoEntity.valueOf(status.name());
    }
}
