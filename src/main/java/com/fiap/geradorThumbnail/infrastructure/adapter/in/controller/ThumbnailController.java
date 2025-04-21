package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import com.fiap.geradorThumbnail.application.service.SnsNotificationService;
import com.fiap.geradorThumbnail.core.usecases.BuscarStatusProcessamentoUseCase;
import com.fiap.geradorThumbnail.core.usecases.BuscarThumbnailsZipUseCase;
import com.fiap.geradorThumbnail.core.usecases.SalvarVideoUseCase;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.request.VideoRequest;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.response.ProcessamentoResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/thumbnail")
public class ThumbnailController {

    private final SalvarVideoUseCase salvarVideoUseCase;
    private final BuscarThumbnailsZipUseCase buscarThumbnailsZipUseCaseq;
    private final BuscarStatusProcessamentoUseCase buscarStatusProcessamentoUseCase;

    public ThumbnailController(SalvarVideoUseCase salvarVideoUseCase, BuscarThumbnailsZipUseCase buscarThumbnailsZipUseCaseq, BuscarStatusProcessamentoUseCase buscarStatusProcessamentoUseCase, 
    SnsNotificationService snsService) {
        this.salvarVideoUseCase = salvarVideoUseCase;
        this.buscarThumbnailsZipUseCaseq = buscarThumbnailsZipUseCaseq;
        this.buscarStatusProcessamentoUseCase = buscarStatusProcessamentoUseCase;
    }

    @PostMapping(value = "/enviar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void enviarMensagem(@Parameter(description = "Vídeos para processar", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary"))) @RequestPart("videos") List<MultipartFile> videos, @RequestPart("idUsuario") String idUsuario) throws IOException {
        VideoRequest videoRequest = new VideoRequest(videos, idUsuario);
        salvarVideoUseCase.executar(videoRequest.toDomain());
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> baixarThumbnailsPorUsuario(@RequestParam String idUsuario) {
        byte[] zip = buscarThumbnailsZipUseCaseq.execute(idUsuario);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "thumbnails_" + idUsuario + ".zip");
        return new ResponseEntity<>(zip, headers, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<ProcessamentoResponse>> listarStatusPorUsuario(@RequestParam String idUsuario) {
        List<ProcessamentoResponse> status = buscarStatusProcessamentoUseCase.execute(idUsuario)
                .stream().map(ProcessamentoResponse::toResponse).toList();

        return ResponseEntity.ok(status);
    }
}
