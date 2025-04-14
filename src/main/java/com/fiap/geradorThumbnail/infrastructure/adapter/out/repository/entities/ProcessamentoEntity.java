package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamento;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "processamentos")
public class ProcessamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente", nullable = false, length = 64)
    private String idCliente;

    @Column(name = "nome_video", nullable = false)
    private String nomeVideo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_processamento", nullable = false, length = 32)
    private StatusProcessamento status;

    @Column(name = "caminho_frames", nullable = false)
    private String caminhoFrames;

    @Column(name = "caminho_video_original", nullable = false)
    private String caminhoVideoOriginal;

    @Column(name = "caminho_zip", nullable = false)
    private String caminhoZip;

    @Column(name = "quantidade_frames", nullable = false)
    private Integer quantidadeFrames;

    @Column(name = "criado_em", updatable = false, insertable = false)
    private LocalDateTime criadoEm;

    public ProcessamentoEntity(Long id, String idCliente, String nomeVideo, StatusProcessamento status, LocalDateTime criadoEm) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeVideo = nomeVideo;
        this.status = status;
        this.caminhoFrames = caminhoFrames;
        this.caminhoVideoOriginal = caminhoVideoOriginal;
        this.caminhoZip = caminhoZip;
        this.quantidadeFrames = quantidadeFrames;
        this.criadoEm = criadoEm;
    }

    public ProcessamentoEntity(String idCliente, String nomeVideo, StatusProcessamento status) {
        this.idCliente = idCliente;
        this.nomeVideo = nomeVideo;
        this.status = status;
        this.caminhoFrames = caminhoFrames;
        this.caminhoVideoOriginal = caminhoVideoOriginal;
        this.caminhoZip = caminhoZip;
        this.quantidadeFrames = quantidadeFrames;
    }

    public ProcessamentoEntity() {
    }

    // Getters e Setters para os novos campos
    public String getCaminhoFrames() {
        return caminhoFrames;
    }

    public void setCaminhoFrames(String caminhoFrames) {
        this.caminhoFrames = caminhoFrames;
    }

    public String getCaminhoVideoOriginal() {
        return caminhoVideoOriginal;
    }

    public void setCaminhoVideoOriginal(String caminhoVideoOriginal) {
        this.caminhoVideoOriginal = caminhoVideoOriginal;
    }

    public String getCaminhoZip() {
        return caminhoZip;
    }

    public void setCaminhoZip(String caminhoZip) {
        this.caminhoZip = caminhoZip;
    }

    public Integer getQuantidadeFrames() {
        return quantidadeFrames;
    }

    public void setQuantidadeFrames(Integer quantidadeFrames) {
        this.quantidadeFrames = quantidadeFrames;
    }
}
