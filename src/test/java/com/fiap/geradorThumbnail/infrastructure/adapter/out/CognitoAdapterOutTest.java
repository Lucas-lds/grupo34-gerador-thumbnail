package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequest;
import com.fiap.geradorThumbnail.infrastructure.exception.CadastroCognitoException;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioPossuiCadastroCognito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

public class CognitoAdapterOutTest {

    private CognitoAdapterOut cognitoAdapterOut;
    private CognitoIdentityProviderClient cognitoClientMock;

    @BeforeEach
    public void setUp() {
        cognitoClientMock = Mockito.mock(CognitoIdentityProviderClient.class);
        cognitoAdapterOut = new CognitoAdapterOut(); // Use default constructor
        // Use reflection to set the mocked client
        ReflectionTestUtils.setField(cognitoAdapterOut, "cognitoClient", cognitoClientMock);
    }

    @Test
    public void testCadastrarUsuarioCognito_Success() {
        UsuarioCognitoRequest usuario = new UsuarioCognitoRequest("John Doe", "john@example.com", "password123", "1234567890");
        
        cognitoAdapterOut.cadastrarUsuarioCognito(usuario);
        
        verify(cognitoClientMock).adminCreateUser(Mockito.any(AdminCreateUserRequest.class));
    }

    @Test
    public void testCadastrarUsuarioCognito_UsernameExistsException() {
        UsuarioCognitoRequest usuario = new UsuarioCognitoRequest("John Doe", "john@example.com", "password123", "1234567890");
        
        Mockito.doThrow(UsernameExistsException.class).when(cognitoClientMock).adminCreateUser(Mockito.any(AdminCreateUserRequest.class));

        assertThrows(UsuarioPossuiCadastroCognito.class, () -> {
            cognitoAdapterOut.cadastrarUsuarioCognito(usuario);
        });
    }

    @Test
    public void testCadastrarUsuarioCognito_GeneralException() {
        UsuarioCognitoRequest usuario = new UsuarioCognitoRequest("John Doe", "john@example.com", "password123", "1234567890");
        
        Mockito.doThrow(new RuntimeException("Some error")).when(cognitoClientMock).adminCreateUser(Mockito.any(AdminCreateUserRequest.class));

        assertThrows(CadastroCognitoException.class, () -> {
            cognitoAdapterOut.cadastrarUsuarioCognito(usuario);
        });
    }
}