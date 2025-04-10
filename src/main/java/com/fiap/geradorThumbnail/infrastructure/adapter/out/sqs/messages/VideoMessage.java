package com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.messages;

import com.fiap.geradorThumbnail.core.domain.Video;

public record VideoMessage(String videoPath, String idUsuario, String formato) {

    public static VideoMessage toMessage(Video video) {
        return new VideoMessage(video.getNomeArquivo(), video.getIdUsuario(), video.getFormatoVideo());
    }
}
