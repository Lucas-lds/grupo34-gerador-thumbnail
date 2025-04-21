package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.out.*;
import com.fiap.geradorThumbnail.core.domain.Usuario;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;
import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequest;
import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("BDD Tests for Services: ProcessarVideoService, UsuarioService, SnsNotificationService, SalvarVideoService")
public class ServicesBDDTest {

    @Nested
    @DisplayName("ProcessarVideoService Tests")
    class ProcessarVideoServiceTests {

        private DeletarVideo deletarVideo;
        private AtualizarStatusVideo atualizarStatusVideo;
        private GerarThumbnail gerarThumbnail;
        private ProcessarVideoService service;
        private SnsNotificationService snsService;

        @BeforeEach
        void setup() {
            deletarVideo = mock(DeletarVideo.class);
            atualizarStatusVideo = mock(AtualizarStatusVideo.class);
            gerarThumbnail = mock(GerarThumbnail.class);
            snsService = mock(SnsNotificationService.class);
            service = new ProcessarVideoService(deletarVideo, atualizarStatusVideo, gerarThumbnail, snsService);
        }

        @Test
        @DisplayName("Should process video successfully and update statuses accordingly")
        void shouldProcessVideoSuccessfully() {
            ProcessamentoVideo processamentoVideo = new ProcessamentoVideo(1L, "video.mp4", "mp4");

            service.executar(processamentoVideo);

            verify(atualizarStatusVideo).execute(StatusProcessamento.PROCESSANDO, 1L);
            verify(gerarThumbnail).execute(processamentoVideo);
            verify(atualizarStatusVideo).execute(StatusProcessamento.FINALIZADO, 1L);
            verify(deletarVideo).execute("video.mp4");
        }

        @Test
        @DisplayName("Should update status to ERRO and rethrow exception on failure")
        void shouldUpdateStatusToErroOnException() {
            ProcessamentoVideo processamentoVideo = new ProcessamentoVideo(2L, "video2.mp4", "mp4");
            doThrow(new RuntimeException("Error")).when(gerarThumbnail).execute(processamentoVideo);

            assertThatThrownBy(() -> service.executar(processamentoVideo))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error");

            verify(atualizarStatusVideo).execute(StatusProcessamento.PROCESSANDO, 2L);
            verify(gerarThumbnail).execute(processamentoVideo);
            verify(atualizarStatusVideo).execute(StatusProcessamento.ERRO, 2L);
            verify(deletarVideo).execute("video2.mp4");
        }
    }

    @Nested
    @DisplayName("UsuarioService Tests")
    class UsuarioServiceTests {

        private UsuarioAdapterPortOut usuarioAdapterPortOut;
        private CognitoAdapterPortOut cognitoAdapterPortOut;
        private ValidarLambdaPortOut validarLambdaPortOut;
        private UsuarioService service;

        @BeforeEach
        void setup() {
            usuarioAdapterPortOut = mock(UsuarioAdapterPortOut.class);
            cognitoAdapterPortOut = mock(CognitoAdapterPortOut.class);
            validarLambdaPortOut = mock(ValidarLambdaPortOut.class);
            service = new UsuarioService(usuarioAdapterPortOut, cognitoAdapterPortOut, validarLambdaPortOut);
        }

        @Test
        @DisplayName("Should register user successfully")
        void shouldRegisterUser() {
            // Create Usuario object with constructor or builder if available
            // If not available, create a mock or use a test utility method
            Usuario usuario = mock(Usuario.class);
            when(usuario.getNome()).thenReturn("John Doe");
            when(usuario.getEmail()).thenReturn("john@example.com");
            when(usuario.getSenha()).thenReturn("password");
            when(usuario.getTelefone()).thenReturn("123456789");

            when(usuarioAdapterPortOut.cadastrar(usuario)).thenReturn(usuario);

            Usuario result = service.cadastrar(usuario);

            verify(cognitoAdapterPortOut).cadastrarUsuarioCognito(any(UsuarioCognitoRequest.class));
            verify(usuarioAdapterPortOut).cadastrar(usuario);
            assertThat(result).isEqualTo(usuario);
        }

        @Test
        @DisplayName("Should authenticate user successfully")
        void shouldAuthenticateUser() {
            String email = "john@example.com";
            String senha = "password";

            service.autenticarUsuario(email, senha);

            verify(validarLambdaPortOut).validar(email, senha);
        }
    }

    @Nested
    @DisplayName("SnsNotificationService Tests")
    class SnsNotificationServiceTests {

        private SnsClient snsClient;
        private SnsNotificationService service;

        @BeforeEach
        void setup() {
            // Mock SnsClient using Mockito
            snsClient = mock(SnsClient.class);
            // Since setSnsClient method does not exist, use reflection to inject mock
            service = new SnsNotificationService("topicArn", "us-east-1");
            try {
                java.lang.reflect.Field snsClientField = SnsNotificationService.class.getDeclaredField("snsClient");
                snsClientField.setAccessible(true);
                snsClientField.set(service, snsClient);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("Should send notification successfully")
        void shouldSendNotification() {
            String message = "Test message";
            String subject = "Test subject";

            PublishResponse response = PublishResponse.builder().messageId("msg-123").build();
            when(snsClient.publish(any(PublishRequest.class))).thenReturn(response);

            service.sendNotification(message, subject);

            ArgumentCaptor<PublishRequest> captor = ArgumentCaptor.forClass(PublishRequest.class);
            verify(snsClient).publish(captor.capture());

            PublishRequest capturedRequest = captor.getValue();
            assertThat(capturedRequest.message()).isEqualTo(message);
            assertThat(capturedRequest.subject()).isEqualTo(subject);
            assertThat(capturedRequest.topicArn()).isEqualTo("topicArn");
        }
    }

    @Nested
    @DisplayName("SalvarVideoService Tests")
    class SalvarVideoServiceTests {

        private ArmazenarVideo armazenarVideo;
        private EnviarNotificacaoVideo enviarNotificacaoVideo;
        private ArmazenarProcessamento armazenarProcessamento;
        private SalvarVideoService service;

        @BeforeEach
        void setup() {
            armazenarVideo = mock(ArmazenarVideo.class);
            enviarNotificacaoVideo = mock(EnviarNotificacaoVideo.class);
            armazenarProcessamento = mock(ArmazenarProcessamento.class);
            service = new SalvarVideoService(armazenarVideo, enviarNotificacaoVideo, armazenarProcessamento);
        }

        @Test
        @DisplayName("Should save videos and send notifications")
        void shouldSaveVideosAndSendNotifications() {
            // Use more realistic video data
            byte[] videoData = new byte[] {1, 2, 3, 4, 5};
            // Since Video constructor with these parameters does not exist, mock Video object
            Video video = mock(Video.class);
            List<Video> videos = List.of(video);
            List<Long> idsProcessamentos = List.of(1L, 2L);

            when(armazenarProcessamento.execute(videos)).thenReturn(idsProcessamentos);

            service.executar(videos);

            verify(armazenarVideo).execute(videos);
            verify(armazenarProcessamento).execute(videos);
            verify(enviarNotificacaoVideo).execute(videos, idsProcessamentos);
        }
    }
}
