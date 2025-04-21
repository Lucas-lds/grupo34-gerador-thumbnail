package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequest;
import com.fiap.geradorThumbnail.infrastructure.exception.CadastroCognitoException;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioPossuiCadastroCognito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CognitoAdapterOutTest {

    private CognitoIdentityProviderClient cognitoClient;
    private CognitoAdapterOut adapter;

    @BeforeEach
    void setup() {
        cognitoClient = mock(CognitoIdentityProviderClient.class);
        adapter = new CognitoAdapterOut(cognitoClient, "test-pool-id");
    }

    @Test
    void testCadastrarUsuarioCognitoSuccess() {
        UsuarioCognitoRequest request = new UsuarioCognitoRequest("John Doe", "john@example.com", "password", "+1234567890");

        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class))).thenReturn(null);
        when(cognitoClient.adminSetUserPassword(any(AdminSetUserPasswordRequest.class))).thenReturn(null);
        when(cognitoClient.adminGetUser(any(AdminGetUserRequest.class))).thenReturn(
                AdminGetUserResponse.builder()
                        .userAttributes(AttributeType.builder().name("sub").value("cognito-user-id-123").build())
                        .build()
        );

        String cognitoUserId = adapter.cadastrarUsuarioCognito(request);

        assertThat(cognitoUserId).isEqualTo("cognito-user-id-123");

        ArgumentCaptor<AdminCreateUserRequest> captor = ArgumentCaptor.forClass(AdminCreateUserRequest.class);
        verify(cognitoClient).adminCreateUser(captor.capture());
        AdminCreateUserRequest capturedRequest = captor.getValue();
        assertThat(capturedRequest.userPoolId()).isEqualTo("test-pool-id");
        assertThat(capturedRequest.username()).isEqualTo("john@example.com");
    }

    @Test
    void testCadastrarUsuarioCognitoUsernameExists() {
        UsuarioCognitoRequest request = new UsuarioCognitoRequest("John Doe", "john@example.com", "password", "+1234567890");

        doThrow(UsernameExistsException.builder().message("User exists").build())
                .when(cognitoClient).adminCreateUser(any(AdminCreateUserRequest.class));

        assertThatThrownBy(() -> adapter.cadastrarUsuarioCognito(request))
                .isInstanceOf(UsuarioPossuiCadastroCognito.class)
                .hasMessageContaining("O email já está cadastrado no sistema");
    }

    @Test
    void testCadastrarUsuarioCognitoOtherException() {
        UsuarioCognitoRequest request = new UsuarioCognitoRequest("John Doe", "john@example.com", "password", "+1234567890");

        doThrow(new RuntimeException("AWS error"))
                .when(cognitoClient).adminCreateUser(any(AdminCreateUserRequest.class));

        assertThatThrownBy(() -> adapter.cadastrarUsuarioCognito(request))
                .isInstanceOf(CadastroCognitoException.class)
                .hasMessageContaining("Falha ao cadastrar usuário no Cognito");
    }
}
