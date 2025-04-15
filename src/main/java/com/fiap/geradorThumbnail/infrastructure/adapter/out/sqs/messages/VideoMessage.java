package com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.messages;

import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;

public record VideoMessage(String videoPath, String idUsuario, String formato, Long idProcessamento) {

    public static VideoMessage toMessage(Video video, Long idProcessamento) {
        return new VideoMessage(video.getCaminhoVideo(), video.getIdUsuario(), video.getFormatoVideo(), idProcessamento);
    }

    //    public static Video toDomain(VideoMessage videoMessage) {
//        return new Video(null, videoMessage.idUsuario(), videoMessage.formato(), )
//    }
    public static ProcessamentoVideo toProcessamentoVideo(VideoMessage videoMessage) {
        return new ProcessamentoVideo(videoMessage.idProcessamento, videoMessage.videoPath, videoMessage.formato);
    }
}
