package com.fiap.geradorThumbnail.infrastructure.adapter.in.request;

import com.fiap.geradorThumbnail.core.domain.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fiap.geradorThumbnail.infrastructure.utils.UtilityFile.sanitizarNomeArquivo;

public record VideoRequest(List<MultipartFile> videos, String idUsuario) {

    public List<Video> toDomain() throws IOException {
        List<Video> listaDeVideos = new ArrayList<>();

        for (MultipartFile video : videos) {
            String nomeOriginal = video.getOriginalFilename();

            if (nomeOriginal == null || nomeOriginal.isEmpty()) {
                throw new IllegalArgumentException("Arquivo sem nome original detectado.");
            }

            String formatoVideo = nomeOriginal.substring(nomeOriginal.lastIndexOf('.') + 1);
            String nomeSanitizado = sanitizarNomeArquivo(nomeOriginal.replace("." + formatoVideo, ""));
            String caminhoVideo = "videos/" + idUsuario + "/" + nomeSanitizado + "_" + UUID.randomUUID() + "." + formatoVideo;

            listaDeVideos.add(new Video(
                    video.getBytes(),
                    idUsuario,
                    formatoVideo,
                    caminhoVideo
            ));
        }

        return listaDeVideos;
    }
}
