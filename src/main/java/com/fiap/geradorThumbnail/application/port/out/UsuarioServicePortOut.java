package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.domain.Usuario;

public interface UsuarioServicePortOut {
    Usuario cadastrar(Usuario usuario);

    void autenticarUsuario(String email, String senha);
}
