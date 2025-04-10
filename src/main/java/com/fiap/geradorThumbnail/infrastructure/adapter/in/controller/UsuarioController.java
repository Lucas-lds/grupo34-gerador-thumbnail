package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fiap.geradorThumbnail.application.port.out.usecase.UsuarioUseCasePortOut;
import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoResponseDTO;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.UsuarioResponse;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.request.UsuarioRequest;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.ValidarLambdaAdapterOut;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioSemPermissaoCognitoException;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioUseCasePortOut usuarioUseCasePortOut;

    public UsuarioController(UsuarioUseCasePortOut usuarioUseCasePortOut, ValidarLambdaAdapterOut validarLambdaAdapterOut) {
        this.usuarioUseCasePortOut = usuarioUseCasePortOut;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        try {
            var usuarioCadastrado = usuarioUseCasePortOut.cadastrarUsuario(usuarioRequest.toDomain());
            UsuarioResponse.fromDomain(usuarioCadastrado);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UsuarioCognitoResponseDTO(true, "Usuario cadastrado com sucesso", usuarioRequest.email()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new UsuarioCognitoResponseDTO(false, e.getMessage(), usuarioRequest.email()));
        }
    }

    @GetMapping("/auth") 
    public ResponseEntity<UsuarioCognitoResponseDTO> validarUsuario(@RequestParam String email, @RequestParam String senha) {
        try {
            usuarioUseCasePortOut.validarAutenticacaoUsuario(email, senha);
            return ResponseEntity.ok(new UsuarioCognitoResponseDTO(true, "Usuário autenticado com sucesso!", email));
        } catch (UsuarioSemPermissaoCognitoException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UsuarioCognitoResponseDTO(false, e.getMessage(), email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UsuarioCognitoResponseDTO(false, e.getMessage(), email));
        }
    }
}