package com.fiap.geradorThumbnail.application.port.out;

public interface ArmazenarThumbnails {
    void execute(String caminhoImagem, byte[] imagem);
}
