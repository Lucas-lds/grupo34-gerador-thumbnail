package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoResponse;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.request.UsuarioRequest;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.response.ProcessamentoResponse;
import com.fiap.geradorThumbnail.infrastructure.adapter.in.request.VideoRequest;
import com.fiap.geradorThumbnail.core.domain.Usuario;
import com.fiap.geradorThumbnail.core.usecases.BuscarStatusProcessamentoUseCase;
import com.fiap.geradorThumbnail.core.usecases.BuscarThumbnailsZipUseCase;
import com.fiap.geradorThumbnail.application.service.SnsNotificationService;
import com.fiap.geradorThumbnail.core.usecases.SalvarVideoUseCase;
import com.fiap.geradorThumbnail.core.usecases.UsuarioUseCase;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioSemPermissaoCognitoException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("BDD Tests for UsuarioController and ThumbnailController")
public class UsuarioThumbnailControllerBDDTest {

    private UsuarioUseCase usuarioUseCase;
    private SalvarVideoUseCase salvarVideoUseCase;
    private BuscarThumbnailsZipUseCase buscarThumbnailsZipUseCase;
    private BuscarStatusProcessamentoUseCase buscarStatusProcessamentoUseCase;
    private SnsNotificationService snsService;

    private UsuarioController usuarioController;
    private ThumbnailController thumbnailController;

    @BeforeEach
    void setup() {
        usuarioUseCase = mock(UsuarioUseCase.class);
        salvarVideoUseCase = mock(SalvarVideoUseCase.class);
        buscarThumbnailsZipUseCase = mock(BuscarThumbnailsZipUseCase.class);
        buscarStatusProcessamentoUseCase = mock(BuscarStatusProcessamentoUseCase.class);
        snsService = mock(SnsNotificationService.class);

        usuarioController = new UsuarioController(usuarioUseCase, null);
        thumbnailController = new ThumbnailController(salvarVideoUseCase, buscarThumbnailsZipUseCase, buscarStatusProcessamentoUseCase, snsService);
    }

    @Nested
    @DisplayName("UsuarioController Tests")
    class UsuarioControllerTests {

        @Test
        @DisplayName("Given valid user data, when cadastrarUsuario is called, then user is created successfully")
        void cadastrarUsuario_success() {
            UsuarioRequest request = new UsuarioRequest("John Doe", "john@example.com", "password", "123456789");
            Usuario usuarioDomain = request.toDomain();

            when(usuarioUseCase.cadastrarUsuario(any())).thenReturn(usuarioDomain);

            ResponseEntity<?> response = usuarioController.cadastrarUsuario(request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isInstanceOf(UsuarioCognitoResponse.class);
            UsuarioCognitoResponse body = (UsuarioCognitoResponse) response.getBody();
            assertThat(body.success()).isTrue();
            assertThat(body.message()).isEqualTo("Usuario cadastrado com sucesso");
            assertThat(body.email()).isEqualTo(request.email());

            verify(usuarioUseCase).cadastrarUsuario(any());
        }

        @Test
        @DisplayName("Given invalid user data, when cadastrarUsuario is called, then bad request is returned")
        void cadastrarUsuario_failure() {
            UsuarioRequest request = new UsuarioRequest("John Doe", "john@example.com", "password", "123456789");

            when(usuarioUseCase.cadastrarUsuario(any())).thenThrow(new RuntimeException("Erro ao cadastrar"));

            ResponseEntity<?> response = usuarioController.cadastrarUsuario(request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isInstanceOf(UsuarioCognitoResponse.class);
            UsuarioCognitoResponse body = (UsuarioCognitoResponse) response.getBody();
            assertThat(body.success()).isFalse();
            assertThat(body.message()).isEqualTo("Erro ao cadastrar");
            assertThat(body.email()).isEqualTo(request.email());

            verify(usuarioUseCase).cadastrarUsuario(any());
        }

        @Test
        @DisplayName("Given valid credentials, when validarUsuario is called, then authentication is successful")
        void validarUsuario_success() {
            String email = "john@example.com";
            String senha = "password";

            doNothing().when(usuarioUseCase).validarAutenticacaoUsuario(email, senha);

            ResponseEntity<UsuarioCognitoResponse> response = usuarioController.validarUsuario(email, senha);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            UsuarioCognitoResponse body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.success()).isTrue();
            assertThat(body.message()).isEqualTo("Usuário autenticado com sucesso!");
            assertThat(body.email()).isEqualTo(email);

            verify(usuarioUseCase).validarAutenticacaoUsuario(email, senha);
        }

        @Test
        @DisplayName("Given unauthorized credentials, when validarUsuario is called, then unauthorized status is returned")
        void validarUsuario_unauthorized() {
            String email = "john@example.com";
            String senha = "wrongpassword";

            doThrow(new UsuarioSemPermissaoCognitoException("Usuário sem permissão")).when(usuarioUseCase).validarAutenticacaoUsuario(email, senha);

            ResponseEntity<UsuarioCognitoResponse> response = usuarioController.validarUsuario(email, senha);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            UsuarioCognitoResponse body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.success()).isFalse();
            assertThat(body.message()).isEqualTo("Usuário sem permissão");
            assertThat(body.email()).isEqualTo(email);

            verify(usuarioUseCase).validarAutenticacaoUsuario(email, senha);
        }

        @Test
        @DisplayName("Given error during authentication, when validarUsuario is called, then internal server error is returned")
        void validarUsuario_error() {
            String email = "john@example.com";
            String senha = "password";

            doThrow(new RuntimeException("Erro inesperado")).when(usuarioUseCase).validarAutenticacaoUsuario(email, senha);

            ResponseEntity<UsuarioCognitoResponse> response = usuarioController.validarUsuario(email, senha);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            UsuarioCognitoResponse body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.success()).isFalse();
            assertThat(body.message()).isEqualTo("Erro inesperado");
            assertThat(body.email()).isEqualTo(email);

            verify(usuarioUseCase).validarAutenticacaoUsuario(email, senha);
        }
    }

    @Nested
    @DisplayName("ThumbnailController Tests")
    class ThumbnailControllerTests {

        @Test
        @DisplayName("Given valid videos and user ID, when enviarMensagem is called, then video is saved and notification sent")
        void enviarMensagem_success() throws IOException {
            MockMultipartFile video1 = new MockMultipartFile("videos", "video1.mp4", "video/mp4", "dummy content".getBytes());
            MockMultipartFile video2 = new MockMultipartFile("videos", "video2.mp4", "video/mp4", "dummy content".getBytes());
            List<MultipartFile> videos = List.of(video1, video2);
            String idUsuario = "user123";

            doNothing().when(salvarVideoUseCase).executar(any());
            doNothing().when(snsService).sendNotification(anyString(), anyString());

            thumbnailController.enviarMensagem(videos, idUsuario);

            ArgumentCaptor<java.util.List> captor = ArgumentCaptor.forClass(java.util.List.class);
            verify(salvarVideoUseCase).executar(captor.capture());
verify(snsService).sendNotification("Processamento do vídeo finalizado.", "Thumbnail Generator");

            List capturedList = captor.getValue();
            assertThat(capturedList).isNotEmpty();
            Object firstElement = capturedList.get(0);
            assertThat(firstElement).isInstanceOf(com.fiap.geradorThumbnail.core.domain.Video.class);

            verify(salvarVideoUseCase).executar(any(List.class));
        }

        @Test
        @DisplayName("Given user ID, when baixarThumbnailsPorUsuario is called, then zip bytes are returned with correct headers")
        void baixarThumbnailsPorUsuario_success() {
            String idUsuario = "user123";
            byte[] zipBytes = "dummyzipcontent".getBytes();

            when(buscarThumbnailsZipUseCase.execute(idUsuario)).thenReturn(zipBytes);

            ResponseEntity<byte[]> response = thumbnailController.baixarThumbnailsPorUsuario(idUsuario);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM);
            assertThat(response.getHeaders().getContentDisposition().getFilename()).isEqualTo("thumbnails_" + idUsuario + ".zip");
            assertThat(response.getBody()).isEqualTo(zipBytes);

            verify(buscarThumbnailsZipUseCase).execute(idUsuario);
        }

        @Test
        @DisplayName("Given user ID, when listarStatusPorUsuario is called, then processing status list is returned")
        void listarStatusPorUsuario_success() {
            String idUsuario = "user123";
            List<com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus> statusList = new ArrayList<>();
            statusList.add(new com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus(1L, "video1", "COMPLETED", null));
            statusList.add(new com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus(2L, "video2", "PROCESSING", null));

            when(buscarStatusProcessamentoUseCase.execute(idUsuario)).thenReturn(statusList);

            ResponseEntity<List<ProcessamentoResponse>> response = thumbnailController.listarStatusPorUsuario(idUsuario);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            List<ProcessamentoResponse> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.size()).isEqualTo(2);
            assertThat(body.get(0).nomeVideo()).isEqualTo("video1");
            assertThat(body.get(0).status()).isEqualTo("COMPLETED");
            assertThat(body.get(1).nomeVideo()).isEqualTo("video2");
            assertThat(body.get(1).status()).isEqualTo("PROCESSING");

            verify(buscarStatusProcessamentoUseCase).execute(idUsuario);
        }
    }
}
