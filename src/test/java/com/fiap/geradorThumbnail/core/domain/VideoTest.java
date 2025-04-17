package com.fiap.geradorThumbnail.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoTest {

    @Test
    void givenValidParameters_whenCreateVideo_thenFieldsAreSet() {
        // Given
        byte[] videoData = new byte[]{1, 2, 3};
        String idUsuario = "user123";
        String formatoVideo = "mp4";
        String nomeArquivo = "video.mp4";
        String caminhoFrames = "frames/";
        String caminhoVideoOriginal = "original/path";
        String caminhoZip = "zip/path";
        int quantidadeFrames = 10;

        // When
        Video video = new Video(videoData, idUsuario, formatoVideo, nomeArquivo, caminhoFrames, caminhoVideoOriginal, caminhoZip, quantidadeFrames);

        // Then
        assertArrayEquals(videoData, video.getVideo());
        assertEquals(idUsuario, video.getIdUsuario());
        assertEquals(formatoVideo, video.getFormatoVideo());
        assertEquals(nomeArquivo, video.getNomeArquivo());
        assertEquals(caminhoFrames, video.getCaminhoFrames());
        assertEquals(caminhoVideoOriginal, video.getCaminhoVideoOriginal());
        assertEquals(caminhoZip, video.getCaminhoZip());
        assertEquals(quantidadeFrames, video.getQuantidadeFrames());
    }
}
