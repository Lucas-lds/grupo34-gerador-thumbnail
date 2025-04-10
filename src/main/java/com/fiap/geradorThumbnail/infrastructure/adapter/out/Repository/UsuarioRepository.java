package com.fiap.geradorThumbnail.infrastructure.adapter.out.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    
}
