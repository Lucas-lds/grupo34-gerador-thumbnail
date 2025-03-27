package com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.CompletableFuture;

@Component
public class SqsPublisherProcessamento {

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;


    public SqsPublisherProcessamento(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    public CompletableFuture<Void> sendMessage(String messageBody) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        return sqsAsyncClient.sendMessage(request)
                .thenAccept(response -> System.out.println("📤 Mensagem enviada com sucesso! MessageId: " + response.messageId()));
    }
}
