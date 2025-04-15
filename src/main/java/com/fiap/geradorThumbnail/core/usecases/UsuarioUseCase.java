package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.core.domain.Usuario;

public interface UsuarioUseCase {

    Usuario cadastrarUsuario(Usuario usuario);

    void validarAutenticacaoUsuario(String email, String senha);
}
