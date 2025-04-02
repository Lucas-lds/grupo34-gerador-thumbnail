package com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs;

import com.fiap.geradorThumbnail.application.port.out.EnviarNotificacaoVideo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class SqsEnviarNotificacaoAdapter implements EnviarNotificacaoVideo {

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;


    public SqsEnviarNotificacaoAdapter(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    @Override
    public void execute(String mensagem) {
        SendMessageRequest request = SendMessageRequest.builder().queueUrl(queueUrl).messageBody(mensagem).build();

        sqsAsyncClient.sendMessage(request).thenAccept(response ->
                System.out.println("📤 Mensagem enviada com sucesso! MessageId: " + response.messageId()));

    }
}
