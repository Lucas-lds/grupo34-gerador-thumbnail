package com.fiap.geradorThumbnail.infrastructure.adapter.in;

import com.fiap.geradorThumbnail.core.usecases.SalvarVideoUseCase;
import com.fiap.geradorThumbnail.core.domain.enums.FormatoVideo;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.request.VideoRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/thumbnail")
public class ThumbnailController {

    private final SalvarVideoUseCase salvarVideoUseCase;

    public ThumbnailController(SalvarVideoUseCase salvarVideoUseCase) {
        this.salvarVideoUseCase = salvarVideoUseCase;
    }

    @PostMapping(value = "/enviar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void enviarMensagem(@RequestPart("video") MultipartFile video,
                               @RequestPart("formatoVideo") String formatoVideo,
                               @RequestPart("idUsuario") String idUsuario) throws IOException {
        VideoRequest videoRequest = new VideoRequest(video, FormatoVideo.fromString(formatoVideo), idUsuario, video.getOriginalFilename());
        salvarVideoUseCase.executar(videoRequest.toDomain());
    }
}
