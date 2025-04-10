package com.fiap.geradorThumbnail.infrastructure.adapter.in.request;

import com.fiap.geradorThumbnail.core.domain.Usuario;

public record UsuarioRequest(String nome, String email, String senha, String telefone) {
    
    public Usuario toDomain() {
        return new Usuario(null, nome, email, senha, telefone);
    }
}
