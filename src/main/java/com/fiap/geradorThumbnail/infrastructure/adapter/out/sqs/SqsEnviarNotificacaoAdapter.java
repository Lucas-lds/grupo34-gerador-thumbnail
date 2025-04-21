package com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs;

import com.fiap.geradorThumbnail.application.port.out.EnviarNotificacaoVideo;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.core.dto.SolicitacaoProcessamentoVideo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

import static com.fiap.geradorThumbnail.infrastructure.utils.JsonUtils.toJson;

@Component
public class SqsEnviarNotificacaoAdapter implements EnviarNotificacaoVideo {

    private final SqsAsyncClient sqsAsyncClient;

    private String queueUrl;

    public SqsEnviarNotificacaoAdapter(SqsAsyncClient sqsAsyncClient) {
        this(sqsAsyncClient, System.getenv("CLOUD_AWS_SQS_QUEUE_NAME_RESOLVER_QUEUES_SQS_SOLICITACAO_PROCESSAMENTO_FIFO"));
    }

    public SqsEnviarNotificacaoAdapter(SqsAsyncClient sqsAsyncClient, String queueUrl) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.queueUrl = queueUrl;

        if (queueUrl == null || queueUrl.isEmpty()) {
            throw new IllegalStateException("A variável de ambiente CLOUD_AWS_SQS_QUEUE_NAME_RESOLVER_QUEUES_SQS_SOLICITACAO_PROCESSAMENTO_FIFO não foi configurada.");
        }
    }

    @Override
    public void execute(List<Video> videos, List<Long> idsProcessamentos) {
        for (int i = 0; i < videos.size(); i++) {
            Video video = videos.get(i);
            Long idProcessamento = idsProcessamentos.get(i);

            var videoMessage = SolicitacaoProcessamentoVideo.toMessage(video, idProcessamento);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(toJson(videoMessage))
                    .build();

            sqsAsyncClient.sendMessage(request).thenAccept(response ->
                    System.out.println("📤 Mensagem enviada com sucesso para o SQS! MessageId: " + response.messageId())
            );
        }
    }
}
