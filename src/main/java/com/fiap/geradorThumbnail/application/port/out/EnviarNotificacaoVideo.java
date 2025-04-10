package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.domain.Video;

public interface EnviarNotificacaoVideo {

    void execute(Video video);
}
