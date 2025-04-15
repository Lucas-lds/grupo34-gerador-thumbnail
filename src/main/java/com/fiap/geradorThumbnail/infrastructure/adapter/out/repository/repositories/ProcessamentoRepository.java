package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessamentoRepository extends JpaRepository<ProcessamentoEntity, Long> {

    List<ProcessamentoEntity> findByIdCliente(String idCliente);

    List<ProcessamentoEntity> findByIdClienteOrderByStatusAscCriadoEmDesc(String idCliente);
}

