package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioSemPermissaoCognitoException;
import com.fiap.geradorThumbnail.application.port.out.ValidarLambdaPortOut;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("BDD Tests for ValidarLambdaAdapterOut")
public class ValidarLambdaAdapterOutBDDTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private ValidarLambdaPortOut validarLambdaAdapterOut;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        validarLambdaAdapterOut = new ValidarLambdaAdapterOut(restTemplate, objectMapper);

        // Set the apiGatewayUrl field via reflection since it is @Value injected
        try {
            java.lang.reflect.Field field = ValidarLambdaAdapterOut.class.getDeclaredField("apiGatewayUrl");
            field.setAccessible(true);
            field.set(validarLambdaAdapterOut, "http://fake-api-gateway-url");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Nested
    @DisplayName("ValidarLambdaAdapterOut validar method tests")
    class ValidarMethodTests {

        @Test
        @DisplayName("Should call API with correct request and headers")
        void shouldCallApiWithCorrectRequest() throws Exception {
            String email = "test@example.com";
            String senha = "password";

            // Prepare mocks for ObjectMapper
            String bodyContentJson = "{\"email\":\"test@example.com\",\"senha\":\"password\"}";
            String requestBodyJson = "{\"body\":\"" + bodyContentJson.replace("\"", "\\\"") + "\"}";

            when(objectMapper.writeValueAsString(any())).thenReturn(bodyContentJson, requestBodyJson);

            ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"statusCode\":200}", HttpStatus.OK);
            when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(responseEntity);

            when(objectMapper.readTree("{\"statusCode\":200}")).thenReturn(mock(JsonNode.class));
            JsonNode jsonNode = objectMapper.readTree("{\"statusCode\":200}");
            when(jsonNode.has("statusCode")).thenReturn(true);
            when(jsonNode.get("statusCode")).thenReturn(mock(JsonNode.class));
            when(jsonNode.get("statusCode").asInt()).thenReturn(200);

            validarLambdaAdapterOut.validar(email, senha);

            ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
            verify(restTemplate).exchange(eq("http://fake-api-gateway-url"), eq(HttpMethod.POST), httpEntityCaptor.capture(), eq(String.class));

            HttpEntity<String> capturedEntity = httpEntityCaptor.getValue();
            HttpHeaders headers = capturedEntity.getHeaders();
            assertThat(headers.getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
            assertThat(capturedEntity.getBody()).isNotNull();
        }

        @Test
        @DisplayName("Should throw UsuarioSemPermissaoCognitoException on UNAUTHORIZED response")
        void shouldThrowExceptionOnUnauthorized() throws Exception {
            String email = "test@example.com";
            String senha = "password";

            when(objectMapper.writeValueAsString(any())).thenReturn("{}", "{}");

            ResponseEntity<String> responseEntity = new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(responseEntity);

            assertThatThrownBy(() -> validarLambdaAdapterOut.validar(email, senha))
                    .isInstanceOf(UsuarioSemPermissaoCognitoException.class)
                    .hasMessage("Usuário não tem permissão no Cognito");
        }

        @Test
        @DisplayName("Should throw UsuarioSemPermissaoCognitoException when statusCode in response JSON is not 200")
        void shouldThrowExceptionWhenStatusCodeNot200() throws Exception {
            String email = "test@example.com";
            String senha = "password";

            when(objectMapper.writeValueAsString(any())).thenReturn("{}", "{}");

            ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"statusCode\":400}", HttpStatus.OK);
            when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(responseEntity);

            JsonNode jsonNode = mock(JsonNode.class);
            when(objectMapper.readTree("{\"statusCode\":400}")).thenReturn(jsonNode);
            when(jsonNode.has("statusCode")).thenReturn(true);
            when(jsonNode.get("statusCode")).thenReturn(mock(JsonNode.class));
            when(jsonNode.get("statusCode").asInt()).thenReturn(400);

            assertThatThrownBy(() -> validarLambdaAdapterOut.validar(email, senha))
                    .isInstanceOf(UsuarioSemPermissaoCognitoException.class)
                    .hasMessage("Usuário não autorizado");
        }

        @Test
        @DisplayName("Should not throw exception on successful validation")
        void shouldNotThrowExceptionOnSuccess() throws Exception {
            String email = "test@example.com";
            String senha = "password";

            when(objectMapper.writeValueAsString(any())).thenReturn("{}", "{}");

            ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"statusCode\":200}", HttpStatus.OK);
            when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(responseEntity);

            JsonNode jsonNode = mock(JsonNode.class);
            when(objectMapper.readTree("{\"statusCode\":200}")).thenReturn(jsonNode);
            when(jsonNode.has("statusCode")).thenReturn(true);
            when(jsonNode.get("statusCode")).thenReturn(mock(JsonNode.class));
            when(jsonNode.get("statusCode").asInt()).thenReturn(200);

            assertThatCode(() -> validarLambdaAdapterOut.validar(email, senha))
                    .doesNotThrowAnyException();
        }
    }
}
