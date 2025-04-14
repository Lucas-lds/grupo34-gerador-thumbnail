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

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;


    public SqsEnviarNotificacaoAdapter(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    @Override
    public void execute(Video video, Long idProcessamento) {
        var videoMessage = VideoMessage.toMessage(video, idProcessamento);
        SendMessageRequest request = SendMessageRequest.builder().queueUrl(queueUrl).messageBody(toJson(videoMessage)).build();

        sqsAsyncClient.sendMessage(request).thenAccept(response ->
                System.out.println("📤 Mensagem enviada com sucesso para o SQS! MessageId: " + response.messageId()));

    }
}
