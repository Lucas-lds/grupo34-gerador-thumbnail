package com.fiap.geradorThumbnail.core.usecase;

import com.fiap.geradorThumbnail.application.port.out.UsuarioServicePortOut;
import com.fiap.geradorThumbnail.application.port.out.usecase.UsuarioUseCasePortOut;
import com.fiap.geradorThumbnail.core.domain.Usuario;

public class UsuarioUseCase implements UsuarioUseCasePortOut {
    
    private final UsuarioServicePortOut usuarioService;

    public UsuarioUseCase(UsuarioServicePortOut usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Usuario cadastrarUsuario(Usuario usuario) {
        return usuarioService.cadastrar(usuario);
    }

    @Override
    public void validarAutenticacaoUsuario(String email, String senha) {
        usuarioService.autenticarUsuario(email, senha);
    }
}
