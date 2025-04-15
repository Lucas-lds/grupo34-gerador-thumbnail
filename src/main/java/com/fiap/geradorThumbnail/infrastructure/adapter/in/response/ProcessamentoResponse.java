package com.fiap.geradorThumbnail.infrastructure.adapter.in.response;

import java.time.LocalDateTime;

public record ProcessamentoResponse(
        Long id,
        String nomeVideo,
        String status,
        LocalDateTime criadoEm
) {
}
