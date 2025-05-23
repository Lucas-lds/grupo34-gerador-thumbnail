package com.fiap.geradorThumbnail.application.service;

import org.springframework.stereotype.Service;

import com.fiap.geradorThumbnail.application.port.out.CognitoAdapterPortOut;
import com.fiap.geradorThumbnail.application.port.out.UsuarioAdapterPortOut;
import com.fiap.geradorThumbnail.application.port.out.UsuarioServicePortOut;
import com.fiap.geradorThumbnail.application.port.out.ValidarLambdaPortOut;
import com.fiap.geradorThumbnail.core.domain.Usuario;
import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequest;

@Service
public class UsuarioService implements UsuarioServicePortOut {
    
    private final UsuarioAdapterPortOut usuarioAdapterPortOut;
    private final CognitoAdapterPortOut cognitoAdapterPortOut;
    private final ValidarLambdaPortOut validarLambdaPortOut;

    public UsuarioService(UsuarioAdapterPortOut usuarioAdapterPortOut, CognitoAdapterPortOut cognitoAdapterPortOut,
                          ValidarLambdaPortOut validarLambdaPortOut) {
        this.usuarioAdapterPortOut = usuarioAdapterPortOut;
        this.cognitoAdapterPortOut = cognitoAdapterPortOut;
        this.validarLambdaPortOut = validarLambdaPortOut;
    }

    @Override
    public Usuario cadastrar(Usuario usuario) {
        // 1. Cria usuário no Cognito e obtém o cognito_user_id
        String cognitoUserId = cognitoAdapterPortOut.cadastrarUsuarioCognito(new UsuarioCognitoRequest(usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
            usuario.getTelefone()));

        // 2. Preenche o campo cognito_user_id no objeto Usuario
        usuario.setCognitoUserId(cognitoUserId);

        return usuarioAdapterPortOut.cadastrar(usuario);
    }

    @Override
    public void autenticarUsuario(String email, String senha) {
        validarLambdaPortOut.validar(email, senha);
    }
}
