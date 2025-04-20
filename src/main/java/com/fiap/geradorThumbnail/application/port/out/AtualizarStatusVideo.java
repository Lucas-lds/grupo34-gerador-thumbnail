package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;

public interface AtualizarStatusVideo {
    void execute(StatusProcessamento status, Long idProcessamento);
}
