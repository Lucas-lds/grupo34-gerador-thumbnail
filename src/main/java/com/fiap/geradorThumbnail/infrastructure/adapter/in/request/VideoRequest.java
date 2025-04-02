package com.fiap.geradorThumbnail.infrastructure.adapter.in.request;

import com.fiap.geradorThumbnail.core.domain.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record VideoRequest(MultipartFile video, String nomeVideo) {

    public Video toDomain() throws IOException {
        return new Video(video.getBytes(), nomeVideo);
    }
}
