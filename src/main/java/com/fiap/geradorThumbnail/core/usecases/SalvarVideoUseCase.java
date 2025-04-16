package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.core.domain.Video;

import java.util.List;

public interface SalvarVideoUseCase {

    void executar(List<Video> video);
}

