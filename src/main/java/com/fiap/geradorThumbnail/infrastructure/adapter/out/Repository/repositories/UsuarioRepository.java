package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    
}
