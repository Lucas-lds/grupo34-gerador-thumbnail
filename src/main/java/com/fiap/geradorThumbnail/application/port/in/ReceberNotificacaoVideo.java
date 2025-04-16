package com.fiap.geradorThumbnail.application.port.in;

import com.fiap.geradorThumbnail.core.dto.SolicitacaoProcessamentoVideo;

public interface ReceberNotificacaoVideo {
    void listen(SolicitacaoProcessamentoVideo messageBody);
}
