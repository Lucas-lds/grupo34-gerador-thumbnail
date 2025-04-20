package com.fiap.geradorThumbnail.core.dto;

import java.time.LocalDateTime;

public record ProcessamentoStatus(Long id, String nomeVideo, String status, LocalDateTime criadoEm) {
}
