package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities;

import com.fiap.geradorThumbnail.core.domain.Usuario;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioEntityTest {

    @Test
    void testDefaultConstructorAndSettersGetters() {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setCognitoUserId("cognito-id-123");

        assertThat(entity.getCognitoUserId()).isEqualTo("cognito-id-123");
    }

    @Test
    void testFullConstructorAndGetters() {
        UsuarioEntity entity = new UsuarioEntity(1L, "John Doe", "cognito-id-123", "john@example.com", "password", "1234567890");

        assertThat(entity).isNotNull();
        assertThat(entity.getCognitoUserId()).isEqualTo("cognito-id-123");
    }

    @Test
    void testToDomain() {
        UsuarioEntity entity = new UsuarioEntity(1L, "John Doe", "cognito-id-123", "john@example.com", "password", "1234567890");
        Usuario usuario = entity.toDomain();

        assertThat(usuario).isNotNull();
        assertThat(usuario.getIdUsuario()).isEqualTo(1L);
        assertThat(usuario.getNome()).isEqualTo("John Doe");
        assertThat(usuario.getCognitoUserId()).isEqualTo("cognito-id-123");
        assertThat(usuario.getEmail()).isEqualTo("john@example.com");
        assertThat(usuario.getSenha()).isEqualTo("password");
        assertThat(usuario.getTelefone()).isEqualTo("1234567890");
    }

    @Test
    void testFromDomain() {
        Usuario usuario = new Usuario(1L, "John Doe", "john@example.com", "password", "1234567890");
        usuario.setCognitoUserId("cognito-id-123");

        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);

        assertThat(entity).isNotNull();
        assertThat(entity.getCognitoUserId()).isEqualTo("cognito-id-123");
        assertThat(entity.toDomain().getNome()).isEqualTo("John Doe");
    }
}
