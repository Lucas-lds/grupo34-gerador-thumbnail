package com.fiap.geradorThumbnail.infrastructure.adapter.in.request;

import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.core.domain.enums.FormatoVideo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.fiap.geradorThumbnail.infrastructure.utils.UtilityFile.removerExtensaoZip;
import static com.fiap.geradorThumbnail.infrastructure.utils.UtilityFile.sanitizarNomeArquivo;

public record VideoRequest(MultipartFile video, FormatoVideo formatoVideo, String idUsuario,
                           String nomeArquivoOriginal) {

    public Video toDomain() throws IOException {
        var randomUuid = UUID.randomUUID().toString();
        var nomeArquivoFormatado = sanitizarNomeArquivo(removerExtensaoZip(nomeArquivoOriginal));
        var nomeArquivo = "videos/" + idUsuario + "/" + nomeArquivoFormatado + "_" + randomUuid + ".zip";
        return new Video(
            video.getBytes(), 
            idUsuario, 
            formatoVideo.getExtensao(), 
            nomeArquivo,
            null,  // caminhoFrames
            null,  // caminhoVideoOriginal
            null,  // caminhoZip
            0      // quantidadeFrames
        );
    }
}
