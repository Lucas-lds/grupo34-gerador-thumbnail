package com.fiap.geradorThumbnail.core.domain;

public class Thumbnail {
    private String caminhoVideoOriginal;
    private String caminhoFrames;
    private String caminhoZip;
    private int quantidadeFrames;

    public Thumbnail(String caminhoVideoOriginal, String caminhoFrames, String caminhoZip, int quantidadeFrames) {
        this.caminhoVideoOriginal = caminhoVideoOriginal;
        this.caminhoFrames = caminhoFrames;
        this.caminhoZip = caminhoZip;
        this.quantidadeFrames = quantidadeFrames;
    }

    public String getCaminhoVideoOriginal() {
        return caminhoVideoOriginal;
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
}
