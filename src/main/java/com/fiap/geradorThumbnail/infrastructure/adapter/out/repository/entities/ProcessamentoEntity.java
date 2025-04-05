package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    @Column(name = "criado_em", updatable = false, insertable = false)
    private LocalDateTime criadoEm;

    public ProcessamentoEntity(Long id, String idCliente, String nomeVideo, StatusProcessamento status, LocalDateTime criadoEm) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeVideo = nomeVideo;
        this.status = status;
        this.criadoEm = criadoEm;
    }

    public ProcessamentoEntity(String idCliente, String nomeVideo, StatusProcessamento status) {
        this.idCliente = idCliente;
        this.nomeVideo = nomeVideo;
        this.status = status;
    }

    public ProcessamentoEntity() {
    }
}
