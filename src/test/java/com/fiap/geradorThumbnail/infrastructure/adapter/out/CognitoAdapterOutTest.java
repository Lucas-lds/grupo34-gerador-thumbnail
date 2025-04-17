package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequestDTO;
import com.fiap.geradorThumbnail.infrastructure.exception.CadastroCognitoException;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioPossuiCadastroCognito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CognitoAdapterOutTest {

    private CognitoIdentityProviderClient cognitoClient;
    private CognitoAdapterOut cognitoAdapterOut;

    @BeforeEach
    void setUp() throws Exception {
        cognitoClient = mock(CognitoIdentityProviderClient.class);
        cognitoAdapterOut = new CognitoAdapterOut() {
            {
                // Override cognitoClient with mock
                try {
                    Field clientField = CognitoAdapterOut.class.getDeclaredField("cognitoClient");
                    clientField.setAccessible(true);
                    clientField.set(this, cognitoClient);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        // Set userPoolId and clientId via reflection
        Field userPoolIdField = CognitoAdapterOut.class.getDeclaredField("userPoolId");
        userPoolIdField.setAccessible(true);
        userPoolIdField.set(cognitoAdapterOut, "fakeUserPoolId");

        Field clientIdField = CognitoAdapterOut.class.getDeclaredField("clientId");
        clientIdField.setAccessible(true);
        clientIdField.set(cognitoAdapterOut, "fakeClientId");
    }

    @Test
    void givenValidUser_whenCadastrarUsuarioCognito_thenSuccess() {
        UsuarioCognitoRequestDTO user = new UsuarioCognitoRequestDTO("Test User", "test@example.com", "password", "123456789");

        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class))).thenReturn(null);

        assertDoesNotThrow(() -> cognitoAdapterOut.cadastrarUsuarioCognito(user));

        verify(cognitoClient, times(1)).adminCreateUser(any(AdminCreateUserRequest.class));
    }

    @Test
    void givenUsernameExistsException_whenCadastrarUsuarioCognito_thenThrowsUsuarioPossuiCadastroCognito() {
        UsuarioCognitoRequestDTO user = new UsuarioCognitoRequestDTO("Test User", "test@example.com", "password", "123456789");

        doThrow(UsernameExistsException.builder().message("User exists").build())
                .when(cognitoClient).adminCreateUser(any(AdminCreateUserRequest.class));

        UsuarioPossuiCadastroCognito exception = assertThrows(UsuarioPossuiCadastroCognito.class,
                () -> cognitoAdapterOut.cadastrarUsuarioCognito(user));
        assertEquals("O email já está cadastrado no sistema", exception.getMessage());
        assertEquals(user.email(), exception.getEmail());
    }

    @Test
    void givenOtherException_whenCadastrarUsuarioCognito_thenThrowsCadastroCognitoException() {
        UsuarioCognitoRequestDTO user = new UsuarioCognitoRequestDTO("Test User", "test@example.com", "password", "123456789");

        doThrow(new RuntimeException("Other error"))
                .when(cognitoClient).adminCreateUser(any(AdminCreateUserRequest.class));

        CadastroCognitoException exception = assertThrows(CadastroCognitoException.class,
                () -> cognitoAdapterOut.cadastrarUsuarioCognito(user));
        assertTrue(exception.getMessage().contains("Falha ao cadastrar usuário no Cognito"));
    }
}
