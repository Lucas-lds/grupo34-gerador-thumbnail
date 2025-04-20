package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.domain.Video;

import java.util.List;

public interface ArmazenarVideo {

    void execute(List<Video> videos);
}
