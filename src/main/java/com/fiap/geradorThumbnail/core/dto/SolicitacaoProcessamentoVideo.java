package com.fiap.geradorThumbnail.core.dto;

import com.fiap.geradorThumbnail.core.domain.Video;

public record SolicitacaoProcessamentoVideo(String videoPath, String idUsuario, String formato, Long idProcessamento) {

    public static SolicitacaoProcessamentoVideo toMessage(Video video, Long idProcessamento) {
        return new SolicitacaoProcessamentoVideo(video.getCaminhoVideo(), video.getIdUsuario(), video.getFormatoVideo(), idProcessamento);
    }

    public static ProcessamentoVideo toProcessamentoVideo(SolicitacaoProcessamentoVideo solicitacaoProcessamentoVideo) {
        return new ProcessamentoVideo(solicitacaoProcessamentoVideo.idProcessamento, solicitacaoProcessamentoVideo.videoPath, solicitacaoProcessamentoVideo.formato);
    }
}
