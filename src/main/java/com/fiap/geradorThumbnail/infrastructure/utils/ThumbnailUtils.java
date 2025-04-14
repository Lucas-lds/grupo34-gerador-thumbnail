package com.fiap.geradorThumbnail.infrastructure.utils;

import java.util.UUID;

public final class ThumbnailUtils {

    private ThumbnailUtils() {
    }

    public static String extrairUsuario(String caminhoVideo) {
        return caminhoVideo.split("/")[1];
    }

    public static String extrairNomeBase(String caminhoVideo) {
        String nomeArquivo = caminhoVideo.split("/")[2];
        return nomeArquivo.substring(0, nomeArquivo.lastIndexOf("_"));
    }

    public static String gerarCaminhoThumbnail(String caminhoVideo) {
        String usuario = extrairUsuario(caminhoVideo);
        String nomeBase = extrairNomeBase(caminhoVideo);
        return "thumbnails/" + usuario + "/" + nomeBase + "/" + nomeBase + "_thumbnail_" + UUID.randomUUID() + ".jpg";
    }
}
