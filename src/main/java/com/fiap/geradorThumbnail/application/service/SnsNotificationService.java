package com.fiap.geradorThumbnail.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SnsNotificationService {

    private final SnsClient snsClient;
    private final String topicArn;

    public SnsNotificationService(
        @Value("${aws.sns.topicArn}}") String topicArn,
        @Value("${aws.region}") String region
    ) {
        this.topicArn = topicArn;
        this.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void sendNotification(String message, String subject) {
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .subject(subject)
                .build();

        PublishResponse response = snsClient.publish(request);
        System.out.println("Mensagem enviada. ID: " + response.messageId());
    }
}
