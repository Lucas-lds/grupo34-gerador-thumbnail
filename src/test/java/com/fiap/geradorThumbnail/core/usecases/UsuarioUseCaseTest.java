package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.core.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioUseCaseTest {

    private final UsuarioUseCase usuarioUseCase = Mockito.mock(UsuarioUseCase.class);

    @Test
    void givenValidUser_whenCadastraUsuario_thenSavesSuccessfully() {
        // Given
        Usuario usuario = new Usuario(1L, "User Name", "user@example.com", "password", "123456789");
        
        // When
        Mockito.when(usuarioUseCase.cadastrarUsuario(usuario)).thenReturn(usuario);
        Usuario actualUsuario = usuarioUseCase.cadastrarUsuario(usuario);
        
        // Then
        assertThat(actualUsuario).isEqualTo(usuario);
    }

    @Test
    void givenInvalidUser_whenCadastraUsuario_thenReturnsNull() {
        // Given
        Usuario usuario = new Usuario(null, "", "", "", ""); // Invalid user
        
        // When
        Mockito.when(usuarioUseCase.cadastrarUsuario(usuario)).thenReturn(null);
        Usuario actualUsuario = usuarioUseCase.cadastrarUsuario(usuario);
        
        // Then
        assertThat(actualUsuario).isNull();
    }
}
