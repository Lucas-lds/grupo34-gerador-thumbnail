package com.fiap.geradorThumbnail.infrastructure.adapter.in.response;

import com.fiap.geradorThumbnail.core.domain.Usuario;

public record UsuarioResponse(Long idUsuario, String nome, String email, String senha, String telefone) {

    public static UsuarioResponse fromDomain(Usuario usuario) {
        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getTelefone());
    }
}
