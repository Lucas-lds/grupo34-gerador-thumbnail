package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.domain.Video;

public interface ArmazenarProcessamento {

    void execute(Video video);
}
