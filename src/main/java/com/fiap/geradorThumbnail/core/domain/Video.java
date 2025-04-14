package com.fiap.geradorThumbnail.core.domain;

import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;

public class Video {
    private final byte[] video;
    private final String idUsuario;
    private final String formatoVideo;
    private final String nomeArquivo;
    private final StatusProcessamento statusProcessamento;
    private String caminhoVideoOriginal;
    private String caminhoFrames;
    private String caminhoZip;
    private int quantidadeFrames;


    public Video(byte[] video, String idUsuario, String formatoVideo, String nomeArquivo,
                String caminhoFrames, String caminhoVideoOriginal, String caminhoZip,
                int quantidadeFrames) {
        this.video = video;
        this.idUsuario = idUsuario;
        this.formatoVideo = formatoVideo;
        this.nomeArquivo = nomeArquivo;
        this.statusProcessamento = StatusProcessamento.AGUARDANDO;
        this.caminhoFrames = caminhoFrames;
        this.caminhoVideoOriginal = caminhoVideoOriginal;
        this.caminhoZip = caminhoZip;
        this.quantidadeFrames = quantidadeFrames;
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

    public String getCaminhoVideoOriginal() {
        return caminhoVideoOriginal;
    }

    public void setCaminhoVideoOriginal(String caminhoVideoOriginal) {
        this.caminhoVideoOriginal = caminhoVideoOriginal;
    }

    public String getCaminhoFrames() {
        return caminhoFrames;
    }

    public String getCaminhoZip() {
        return caminhoZip;
    }

    public int getQuantidadeFrames() {
        return quantidadeFrames;
    }

    public StatusProcessamento getStatusProcessamento() {
        return statusProcessamento;
    }
}
