package com.fiap.geradorThumbnail.infrastructure.adapter.out.entity;

import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tb_videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity {
    @Id
    private UUID id;
    private String idUsuario;
    private String nomeArquivo;
    private String caminhoFrames;
    private String caminhoVideoOriginal;
    private String caminhoZip;
    private int quantidadeFrames;
    @Enumerated(EnumType.STRING)
    private StatusProcessamento status;
}
