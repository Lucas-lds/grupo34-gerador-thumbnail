package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.fiap.geradorThumbnail.application.port.out.UsuarioAdapterPortOut;
import com.fiap.geradorThumbnail.core.domain.Usuario;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories.UsuarioRepository;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.UsuarioEntity;
import com.fiap.geradorThumbnail.infrastructure.exception.EmailDuplicadoException;

@Component
public class UsuarioAdapterOut implements UsuarioAdapterPortOut {
    
    private final UsuarioRepository repository;

    public UsuarioAdapterOut(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario cadastrar(Usuario usuario) {
        try {
            return repository.save(UsuarioEntity.fromDomain(usuario)).toDomain();
        } catch (DataIntegrityViolationException e) {
            throw new EmailDuplicadoException("O e-mail fornecido já está cadastrado.");
        }
    }
}
