package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;

public interface GerarThumbnail {
    void execute(ProcessamentoVideo processamentoVideo);
}
