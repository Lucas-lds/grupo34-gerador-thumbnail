package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioSemPermissaoCognitoException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValidarLambdaAdapterOutTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private ValidarLambdaAdapterOut validarLambdaAdapterOut;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        validarLambdaAdapterOut = new ValidarLambdaAdapterOut(restTemplate, objectMapper);
        // Set apiGatewayUrl via reflection since it's @Value private
        try {
            java.lang.reflect.Field field = ValidarLambdaAdapterOut.class.getDeclaredField("apiGatewayUrl");
            field.setAccessible(true);
            field.set(validarLambdaAdapterOut, "http://fake-api-gateway-url");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenValidResponse_whenValidar_thenNoException() throws Exception {
        String email = "test@example.com";
        String senha = "password";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"statusCode\":200}", HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<String>>any(),
                eq(String.class)
        )).thenReturn(responseEntity);

        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.has("statusCode")).thenReturn(true);
        when(jsonNode.get("statusCode")).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(jsonNode.get("statusCode").asInt()).thenReturn(200);
        when(objectMapper.readTree("{\"statusCode\":200}")).thenReturn(jsonNode);

        assertDoesNotThrow(() -> validarLambdaAdapterOut.validar(email, senha));
    }

    @Test
    void givenUnauthorizedResponse_whenValidar_thenThrowsException() {
        String email = "test@example.com";
        String senha = "password";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<String>>any(),
                eq(String.class)
        )).thenReturn(responseEntity);

        UsuarioSemPermissaoCognitoException exception = assertThrows(UsuarioSemPermissaoCognitoException.class,
                () -> validarLambdaAdapterOut.validar(email, senha));
        assertEquals("Usuário não tem permissão no Cognito", exception.getMessage());
    }

    @Test
    void givenInvalidStatusCodeInResponse_whenValidar_thenThrowsException() throws Exception {
        String email = "test@example.com";
        String senha = "password";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"statusCode\":400}", HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<String>>any(),
                eq(String.class)
        )).thenReturn(responseEntity);

        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.has("statusCode")).thenReturn(true);
        when(jsonNode.get("statusCode")).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(jsonNode.get("statusCode").asInt()).thenReturn(400);
        when(objectMapper.readTree("{\"statusCode\":400}")).thenReturn(jsonNode);

        UsuarioSemPermissaoCognitoException exception = assertThrows(UsuarioSemPermissaoCognitoException.class,
                () -> validarLambdaAdapterOut.validar(email, senha));
        assertEquals("Usuário não autorizado", exception.getMessage());
    }

    @Test
    void givenExceptionInValidar_whenValidar_thenDoesNotThrow() throws Exception {
        String email = "test@example.com";
        String senha = "password";

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<String>>any(),
                eq(String.class)
        )).thenThrow(new RuntimeException("Some error"));

        assertDoesNotThrow(() -> validarLambdaAdapterOut.validar(email, senha));
    }
}
