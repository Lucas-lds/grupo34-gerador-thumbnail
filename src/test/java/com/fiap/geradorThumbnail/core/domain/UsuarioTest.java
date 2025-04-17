package com.fiap.geradorThumbnail.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void givenValidParameters_whenCreateUsuario_thenFieldsAreSet() {
        // Given
        Long idUsuario = 1L;
        String nome = "Test User";
        String email = "test@example.com";
        String senha = "password";
        String telefone = "123456789";

        // When
        Usuario usuario = new Usuario(idUsuario, nome, email, senha, telefone);

        // Then
        assertEquals(idUsuario, usuario.getIdUsuario());
        assertEquals(nome, usuario.getNome());
        assertEquals(email, usuario.getEmail());
        assertEquals(senha, usuario.getSenha());
        assertEquals(telefone, usuario.getTelefone());
    }
}
