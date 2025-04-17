package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.out.CognitoAdapterPortOut;
import com.fiap.geradorThumbnail.application.port.out.UsuarioAdapterPortOut;
import com.fiap.geradorThumbnail.application.port.out.ValidarLambdaPortOut;
import com.fiap.geradorThumbnail.core.domain.Usuario;
import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UsuarioServiceTest {

    private UsuarioAdapterPortOut usuarioAdapterPortOut;
    private CognitoAdapterPortOut cognitoAdapterPortOut;
    private ValidarLambdaPortOut validarLambdaPortOut;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioAdapterPortOut = Mockito.mock(UsuarioAdapterPortOut.class);
        cognitoAdapterPortOut = Mockito.mock(CognitoAdapterPortOut.class);
        validarLambdaPortOut = Mockito.mock(ValidarLambdaPortOut.class);
        usuarioService = new UsuarioService(usuarioAdapterPortOut, cognitoAdapterPortOut, validarLambdaPortOut);
    }

    @Test
    void givenValidUsuario_whenCadastrar_thenCognitoAndUsuarioAdapterAreCalled() {
        // Given
        Usuario usuario = new Usuario(null, "Test User", "test@example.com", "password", "123456789");

        // When
        usuarioService.cadastrar(usuario);

        // Then
        ArgumentCaptor<UsuarioCognitoRequestDTO> captor = ArgumentCaptor.forClass(UsuarioCognitoRequestDTO.class);
        verify(cognitoAdapterPortOut, times(1)).cadastrarUsuarioCognito(captor.capture());
        UsuarioCognitoRequestDTO dto = captor.getValue();
        assertEquals(usuario.getNome(), dto.nome());
        assertEquals(usuario.getEmail(), dto.email());
        assertEquals(usuario.getSenha(), dto.senha());
        assertEquals(usuario.getTelefone(), dto.telefone());

        verify(usuarioAdapterPortOut, times(1)).cadastrar(usuario);
    }

    @Test
    void autenticarUsuario_shouldCallValidarLambda() {
        String email = "test@example.com";
        String senha = "password";

        usuarioService.autenticarUsuario(email, senha);

        verify(validarLambdaPortOut, times(1)).validar(email, senha);
    }
}
