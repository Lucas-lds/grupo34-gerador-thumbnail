package com.fiap.geradorThumbnail.core.domain.enums;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamentoEntity;

public enum StatusProcessamento {
    AGUARDANDO,
    PROCESSANDO,
    FINALIZADO,
    ERRO;

    public StatusProcessamentoEntity toEntity() {
        return StatusProcessamentoEntity.valueOf(this.name());
    }
}
