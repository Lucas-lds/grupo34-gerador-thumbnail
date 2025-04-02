package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.domain.Video;

public interface ArmazenarVideo {

    void execute(Video video);
}
