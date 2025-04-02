package com.fiap.geradorThumbnail.core.domain;

public class Video {
    private final byte[] video;
    private final String nome;

    public Video(byte[] video, String nome) {
        this.video = video;
        this.nome = nome;
    }

    public byte[] getVideo() {
        return video;
    }

    public String getNome() {
        return nome;
    }
}
