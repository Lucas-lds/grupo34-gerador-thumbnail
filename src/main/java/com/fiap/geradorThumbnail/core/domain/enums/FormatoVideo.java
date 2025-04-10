package com.fiap.geradorThumbnail.core.domain.enums;

public enum FormatoVideo {
    MP4("mp4"),
    MOV("mov"),
    AVI("avi"),
    MKV("mkv"),
    WMV("wmv"),
    FLV("flv");

    private final String extensao;

    FormatoVideo(String extensao) {
        this.extensao = extensao;
    }

    public String getExtensao() {
        return extensao;
    }

    public static FormatoVideo fromString(String valor) {
        for (FormatoVideo formato : values()) {
            if (formato.name().equalsIgnoreCase(valor) || formato.extensao.equalsIgnoreCase(valor)) {
                return formato;
            }
        }
        throw new IllegalArgumentException("Formato de vídeo não suportado: " + valor);
    }
}
