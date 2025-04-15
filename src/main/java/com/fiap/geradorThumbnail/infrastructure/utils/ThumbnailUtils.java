package com.fiap.geradorThumbnail.infrastructure.utils;

import java.util.UUID;

public final class ThumbnailUtils {

    private ThumbnailUtils() {
    }

    public static String extrairUsuario(String caminhoVideo) {
        String[] partes = caminhoVideo.split("/");
        if (partes.length < 2) {
            throw new IllegalArgumentException("Caminho de vídeo inválido para extrair usuário: " + caminhoVideo);
        }
        return partes[1];
    }

    public static String extrairNomeBase(String caminhoVideo) {
        String[] partes = caminhoVideo.split("/");
        if (partes.length < 3) {
            throw new IllegalArgumentException("Caminho de vídeo inválido para extrair nome base: " + caminhoVideo);
        }
        String nomeArquivo = partes[2];
        int posUnderscore = nomeArquivo.lastIndexOf("_");
        if (posUnderscore == -1) {
            throw new IllegalArgumentException("Nome do vídeo não contém UUID: " + nomeArquivo);
        }
        return nomeArquivo.substring(0, posUnderscore);
    }

    public static String gerarCaminhoThumbnail(String caminhoVideo) {
        String usuario = extrairUsuario(caminhoVideo);
        String nomeBase = extrairNomeBase(caminhoVideo);
        String uuid = UUID.randomUUID().toString();

        return String.format("thumbnails/%s/%s/%s_thumbnail_%s.jpg", usuario, nomeBase, nomeBase, uuid);
    }
}
