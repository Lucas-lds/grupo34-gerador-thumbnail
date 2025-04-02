package com.fiap.geradorThumbnail.infrastructure.adapter.out.s3;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarVideo;
import com.fiap.geradorThumbnail.core.domain.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3VideoAdapter implements ArmazenarVideo {

    private final S3Client s3Client;
    private final String bucketName;

    public S3VideoAdapter(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public void execute(Video video) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(video.getNome())
                .contentType("video/mp4")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(video.getVideo()));
        System.out.println("✅ Vídeo enviado com sucesso para o S3: " + video.getNome());
    }
}
