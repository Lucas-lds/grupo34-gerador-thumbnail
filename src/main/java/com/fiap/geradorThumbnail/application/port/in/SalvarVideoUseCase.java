package com.fiap.geradorThumbnail.application.port.in;

import com.fiap.geradorThumbnail.core.domain.Video;

public interface SalvarVideoUseCase {

    void executar(Video video);
}

