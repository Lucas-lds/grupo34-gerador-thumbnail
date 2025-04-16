package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fiap.geradorThumbnail.application.port.out.CognitoAdapterPortOut;
import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequest;
import com.fiap.geradorThumbnail.infrastructure.exception.CadastroCognitoException;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioPossuiCadastroCognito;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

@Component
public class CognitoAdapterOut implements CognitoAdapterPortOut {

    private final CognitoIdentityProviderClient cognitoClient;
    
    @Value("${aws.cognito.userPoolId:}")
    private String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    public CognitoAdapterOut() {
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public void cadastrarUsuarioCognito(UsuarioCognitoRequest usuario) {
        try {
            AdminCreateUserRequest createUserRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(usuario.email())
                    .temporaryPassword(usuario.senha())
                    .userAttributes(
                            AttributeType.builder().name("email").value(usuario.email()).build(),
                            AttributeType.builder().name("email_verified").value("true").build(),
                            AttributeType.builder().name("name").value(usuario.nome()).build(),
                            AttributeType.builder().name("phone_number").value(usuario.telefone()).build(),
                            AttributeType.builder().name("phone_number_verified").value("true").build()
                    )
                    .messageAction("SUPPRESS")
                    .build();

            cognitoClient.adminCreateUser(createUserRequest);
        }
        catch (UsernameExistsException e){
            throw new UsuarioPossuiCadastroCognito("O email já está cadastrado no sistema", usuario.email());
        }
        catch (Exception e) {
            throw new CadastroCognitoException("Falha ao cadastrar usuário no Cognito: " + e.getMessage());
        }
    }
}
