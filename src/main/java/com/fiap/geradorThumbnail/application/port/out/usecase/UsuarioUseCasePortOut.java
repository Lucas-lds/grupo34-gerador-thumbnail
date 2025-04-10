package com.fiap.geradorThumbnail.application.port.out.usecase;

import com.fiap.geradorThumbnail.core.domain.Usuario;

public interface UsuarioUseCasePortOut {

    Usuario cadastrarUsuario(Usuario usuario);

    void validarAutenticacaoUsuario(String email, String senha);
}
