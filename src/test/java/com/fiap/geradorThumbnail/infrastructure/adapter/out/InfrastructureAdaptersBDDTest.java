package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.core.domain.Usuario;
import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus;
import com.fiap.geradorThumbnail.core.dto.SolicitacaoProcessamentoVideo;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.ArmazenarProcessamentoAdapter;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.AtualizarStatusVideoAdapter;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.BuscarStatusProcessamentoAdapter;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.UsuarioEntity;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.SqsEnviarNotificacaoAdapter;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.repositories.ProcessamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InfrastructureAdaptersBDDTest {

    private ProcessamentoRepository processamentoRepository;
    private ArmazenarProcessamentoAdapter armazenarProcessamentoAdapter;
    private SqsAsyncClient sqsAsyncClient;
    private SqsEnviarNotificacaoAdapter sqsEnviarNotificacaoAdapter;
    private AtualizarStatusVideoAdapter atualizarStatusVideoAdapter;
    private BuscarStatusProcessamentoAdapter buscarStatusProcessamentoAdapter;

    @BeforeEach
    public void setup() {
        processamentoRepository = mock(ProcessamentoRepository.class);
        armazenarProcessamentoAdapter = new ArmazenarProcessamentoAdapter(processamentoRepository);

        sqsAsyncClient = mock(SqsAsyncClient.class);
        sqsEnviarNotificacaoAdapter = new SqsEnviarNotificacaoAdapter(sqsAsyncClient);

        atualizarStatusVideoAdapter = new AtualizarStatusVideoAdapter(processamentoRepository);

        buscarStatusProcessamentoAdapter = new BuscarStatusProcessamentoAdapter(processamentoRepository);
    }

    @Test
    public void testArmazenarProcessamentoAdapter_executeStoresVideosAndReturnsIds() {
        Video video1 = new Video(new byte[0], "user1", "mp4", "path1");
        Video video2 = new Video(new byte[0], "user2", "mp4", "path2");

        // Mock repository save to return entity with id set in constructor
        when(processamentoRepository.save(any())).thenAnswer(invocation -> {
            var entity = invocation.getArgument(0);
            var processamentoEntity = (com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity) entity;
            // Return new entity with id set (simulate DB generated id)
            return new com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity(
                    123L,
                    processamentoEntity.getIdCliente(),
                    processamentoEntity.getNomeVideo(),
                    processamentoEntity.getStatus(),
                    processamentoEntity.getCriadoEm()
            );
        });

        List<Long> ids = armazenarProcessamentoAdapter.execute(List.of(video1, video2));

        assertEquals(2, ids.size());
        verify(processamentoRepository, times(2)).save(any());
    }

    @Test
    public void testSqsEnviarNotificacaoAdapter_executeSendsMessages() {
        Video video = new Video(new byte[0], "user1", "mp4", "path");
        List<Video> videos = List.of(video);
        List<Long> ids = List.of(123L);

        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class))).thenReturn(CompletableFuture.completedFuture(null));

        sqsEnviarNotificacaoAdapter.execute(videos, ids);

        ArgumentCaptor<SendMessageRequest> captor = ArgumentCaptor.forClass(SendMessageRequest.class);
        verify(sqsAsyncClient, times(1)).sendMessage(captor.capture());

        SendMessageRequest request = captor.getValue();
        assertTrue(request.messageBody().contains("idProcessamento"));
        assertEquals(System.getProperty("aws.sqs.queue-url"), request.queueUrl());
    }

    @Test
    public void testUsuarioEntity_toDomainAndFromDomain() {
        UsuarioEntity entity = new UsuarioEntity(1L, "Nome", "cognitoUserId", "email@example.com", "senha", "123456789");
        var domain = entity.toDomain();

        assertEquals(entity.toDomain().getIdUsuario(), domain.getIdUsuario());
        assertEquals(entity.toDomain().getNome(), domain.getNome());
        assertEquals(entity.toDomain().getEmail(), domain.getEmail());
        assertEquals(entity.toDomain().getSenha(), domain.getSenha());
        assertEquals(entity.toDomain().getTelefone(), domain.getTelefone());

        UsuarioEntity fromDomain = UsuarioEntity.fromDomain(domain);
        assertEquals(entity.toDomain().getIdUsuario(), fromDomain.toDomain().getIdUsuario());
        assertEquals(entity.toDomain().getNome(), fromDomain.toDomain().getNome());
        assertEquals(entity.toDomain().getEmail(), fromDomain.toDomain().getEmail());
        assertEquals(entity.toDomain().getSenha(), fromDomain.toDomain().getSenha());
        assertEquals(entity.toDomain().getTelefone(), fromDomain.toDomain().getTelefone());
    }

    @Test
    public void testAtualizarStatusVideoAdapter_executeUpdatesStatus() {
        Long idProcessamento = 1L;
        var entity = mock(com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity.class);
        when(processamentoRepository.findById(idProcessamento)).thenReturn(Optional.of(entity));

        atualizarStatusVideoAdapter.execute(StatusProcessamento.FINALIZADO, idProcessamento);

        verify(entity).setStatus(any());
        verify(processamentoRepository).save(entity);
    }

    @Test
    public void testBuscarStatusProcessamentoAdapter_executeReturnsStatusList() {
        String idUsuario = "user1";
        var entity1 = mock(com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity.class);
        var entity2 = mock(com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.ProcessamentoEntity.class);

        when(entity1.getId()).thenReturn(1L);
        when(entity1.getNomeVideo()).thenReturn("video1");
        when(entity1.getStatus()).thenReturn(com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamentoEntity.FINALIZADO);
        when(entity1.getCriadoEm()).thenReturn(LocalDateTime.now());

        when(entity2.getId()).thenReturn(2L);
        when(entity2.getNomeVideo()).thenReturn("video2");
        when(entity2.getStatus()).thenReturn(com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities.enums.StatusProcessamentoEntity.AGUARDANDO);
        when(entity2.getCriadoEm()).thenReturn(LocalDateTime.now());

        when(processamentoRepository.findByIdClienteOrderByStatusAscCriadoEmDesc(idUsuario))
                .thenReturn(List.of(entity1, entity2));

        List<ProcessamentoStatus> statusList = buscarStatusProcessamentoAdapter.execute(idUsuario);

        assertEquals(2, statusList.size());
        assertEquals("video1", statusList.get(0).nomeVideo());
        assertEquals("FINALIZADO", statusList.get(0).status());
    }
}
