package com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs;

import com.fiap.geradorThumbnail.application.port.out.EnviarNotificacaoVideo;
import com.fiap.geradorThumbnail.core.domain.Video;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.messages.VideoMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static com.fiap.geradorThumbnail.infrastructure.utils.JsonUtils.toJson;

@Component
public class SqsEnviarNotificacaoAdapter implements EnviarNotificacaoVideo {

    private final SqsAsyncClient sqsAsyncClient;

    // @Value("${aws.sqs.queue-url}")
    // private String queueUrl;
    private String queueUrl = System.getenv("CLOUD_AWS_SQS_QUEUE_NAME_RESOLVER_QUEUES_SQS_SOLICITACAO_PROCESSAMENTO_FIFO");


    public SqsEnviarNotificacaoAdapter(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;

        if (queueUrl == null || queueUrl.isEmpty()) {
            throw new IllegalStateException("A variável de ambiente CLOUD_AWS_SQS_QUEUE_NAME_RESOLVER_QUEUES_SQS_SOLICITACAO_PROCESSAMENTO_FIFO não foi configurada.");
        }    
    }

    @Override
    public void execute(Video video) {
        var videoMessage = VideoMessage.toMessage(video);
        SendMessageRequest request = SendMessageRequest.builder().queueUrl(queueUrl).messageBody(toJson(videoMessage)).build();

        sqsAsyncClient.sendMessage(request).thenAccept(response ->
                System.out.println("📤 Mensagem enviada com sucesso para o SQS! MessageId: " + response.messageId()));

    }
}
