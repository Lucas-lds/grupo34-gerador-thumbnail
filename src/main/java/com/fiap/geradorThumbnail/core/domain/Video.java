package com.fiap.geradorThumbnail.core.domain;

import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;

public class Video {
    private final byte[] video;
    private final String idUsuario;
    private final String formatoVideo;
    private final String caminhoVideo;
    private final StatusProcessamento statusProcessamento;

    public Video(byte[] video, String idUsuario, String formatoVideo, String nomeArquivo) {
        this.video = video;
        this.idUsuario = idUsuario;
        this.formatoVideo = formatoVideo;
        this.caminhoVideo = nomeArquivo;
        this.statusProcessamento = StatusProcessamento.AGUARDANDO;
    }

    public byte[] getVideo() {
        return video;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getCaminhoVideo() {
        return caminhoVideo;
    }

    public String getFormatoVideo() {
        return formatoVideo;
    }

    public StatusProcessamento getStatusProcessamento() {
        return statusProcessamento;
    }
}
