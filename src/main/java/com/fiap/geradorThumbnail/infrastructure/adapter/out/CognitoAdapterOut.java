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
    
    private final String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    public CognitoAdapterOut() {
        this(CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build(),
            System.getenv("AWS_COGNITO_USER_POOL_ID"));
    }

    // Constructor for testing
    public CognitoAdapterOut(CognitoIdentityProviderClient cognitoClient, String userPoolId) {
        this.cognitoClient = cognitoClient;
        this.userPoolId = userPoolId;
    }

    @Override
    public String cadastrarUsuarioCognito(UsuarioCognitoRequest usuario) {
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

            // Define a senha como permanente para não forçar troca no primeiro login
            cognitoClient.adminSetUserPassword(
                software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest.builder()
                .userPoolId(userPoolId)
                .username(usuario.email())
                .password(usuario.senha())
                .permanent(true)
                .build()
            );

            // Recupera o sub (cognito_user_id)
            var getUserResponse = cognitoClient.adminGetUser(
                software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest.builder()
                .userPoolId(userPoolId)
                .username(usuario.email())
                .build()
            );
            String cognitoUserId = getUserResponse.userAttributes().stream()
                .filter(attr -> attr.name().equals("sub"))
                .findFirst()
                .get()
                .value();

            return cognitoUserId;
        }
        catch (UsernameExistsException e){
            throw new UsuarioPossuiCadastroCognito("O email já está cadastrado no sistema", usuario.email());
        }
        catch (Exception e) {
            throw new CadastroCognitoException("Falha ao cadastrar usuário no Cognito: " + e.getMessage());
        }
    }
}
