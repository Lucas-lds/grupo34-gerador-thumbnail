package com.fiap.geradorThumbnail.core.domain;

public class Video {
    private final byte[] video;
    private final String idUsuario;
    private final String formatoVideo;
    private final String nomeArquivo;


    public Video(byte[] video, String idUsuario, String formatoVideo, String nomeArquivo) {
        this.video = video;
        this.idUsuario = idUsuario;
        this.formatoVideo = formatoVideo;
        this.nomeArquivo = nomeArquivo;
    }

    public byte[] getVideo() {
        return video;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getFormatoVideo() {
        return formatoVideo;
    }
}
