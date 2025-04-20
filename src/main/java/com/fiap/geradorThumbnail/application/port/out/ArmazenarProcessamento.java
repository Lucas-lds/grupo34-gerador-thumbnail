package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.domain.Video;

import java.util.List;

public interface ArmazenarProcessamento {

    List<Long> execute(List<Video> video);
}
