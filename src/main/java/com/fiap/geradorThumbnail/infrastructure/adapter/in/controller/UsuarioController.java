package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fiap.geradorThumbnail.core.usecases.UsuarioUseCase;
import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoResponse;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.response.UsuarioResponse;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.request.UsuarioRequest;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.ValidarLambdaAdapterOut;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioSemPermissaoCognitoException;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase, ValidarLambdaAdapterOut validarLambdaAdapterOut) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        try {
            var usuarioCadastrado = usuarioUseCase.cadastrarUsuario(usuarioRequest.toDomain());
            UsuarioResponse.fromDomain(usuarioCadastrado);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UsuarioCognitoResponse(true, "Usuario cadastrado com sucesso", usuarioRequest.email()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new UsuarioCognitoResponse(false, e.getMessage(), usuarioRequest.email()));
        }
    }

    @GetMapping("/auth") 
    public ResponseEntity<UsuarioCognitoResponse> validarUsuario(@RequestParam String email, @RequestParam String senha) {
        try {
            usuarioUseCase.validarAutenticacaoUsuario(email, senha);
            return ResponseEntity.ok(new UsuarioCognitoResponse(true, "Usuário autenticado com sucesso!", email));
        } catch (UsuarioSemPermissaoCognitoException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UsuarioCognitoResponse(false, e.getMessage(), email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UsuarioCognitoResponse(false, e.getMessage(), email));
        }
    }
}