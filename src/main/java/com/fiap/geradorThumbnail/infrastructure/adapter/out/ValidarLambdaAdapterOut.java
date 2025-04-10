package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.geradorThumbnail.application.port.out.ValidarLambdaPortOut;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioSemPermissaoCognitoException;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidarLambdaAdapterOut implements ValidarLambdaPortOut {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${aws.gateway.url}")
    private String apiGatewayUrl;

    public ValidarLambdaAdapterOut(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void validar(String email, String senha) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Criando o corpo da requisição corretamente
            Map<String, String> bodyContent = new HashMap<>();
            bodyContent.put("email", email);
            bodyContent.put("senha", senha);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("body", objectMapper.writeValueAsString(bodyContent));

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            ResponseEntity<String> response = restTemplate.exchange(apiGatewayUrl, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new UsuarioSemPermissaoCognitoException("Usuário não tem permissão no Cognito");
            }

            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            if (!jsonResponse.has("statusCode") || jsonResponse.get("statusCode").asInt() != 200) {
                throw new UsuarioSemPermissaoCognitoException("Usuário não autorizado");
            }
        } catch (UsuarioSemPermissaoCognitoException e) {
            throw e;
        } catch (Exception e) {
        }
    }
}
