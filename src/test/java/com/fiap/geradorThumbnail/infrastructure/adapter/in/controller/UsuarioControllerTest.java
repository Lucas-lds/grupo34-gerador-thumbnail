package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoResponseDTO;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.request.UsuarioRequest;
import com.fiap.geradorThumbnail.core.domain.Usuario;
import com.fiap.geradorThumbnail.application.port.out.usecase.UsuarioUseCasePortOut;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.ValidarLambdaAdapterOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    private UsuarioUseCasePortOut usuarioUseCase;
    private ValidarLambdaAdapterOut validarLambdaAdapterOut;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioUseCase = Mockito.mock(UsuarioUseCasePortOut.class);
        validarLambdaAdapterOut = Mockito.mock(ValidarLambdaAdapterOut.class);
        usuarioController = new UsuarioController(usuarioUseCase, validarLambdaAdapterOut);
    }

    @Test
    void givenValidUsuarioRequest_whenCadastrarUsuario_thenReturnsCreatedResponse() {
        // Given
        UsuarioRequest usuarioRequest = new UsuarioRequest("Test User", "test@example.com", "password", "123456789");
        Usuario usuario = new Usuario(null, "Test User", "test@example.com", "password", "123456789");

        when(usuarioUseCase.cadastrarUsuario(any())).thenReturn(usuario);

        // When
        ResponseEntity<?> response = usuarioController.cadastrarUsuario(usuarioRequest);

        // Then
        assertEquals(201, response.getStatusCodeValue());
        UsuarioCognitoResponseDTO body = (UsuarioCognitoResponseDTO) response.getBody();
        assertEquals(true, body.success());
        assertEquals("Usuario cadastrado com sucesso", body.message());
        assertEquals(usuarioRequest.email(), body.email());

        verify(usuarioUseCase, times(1)).cadastrarUsuario(any());
    }
}
