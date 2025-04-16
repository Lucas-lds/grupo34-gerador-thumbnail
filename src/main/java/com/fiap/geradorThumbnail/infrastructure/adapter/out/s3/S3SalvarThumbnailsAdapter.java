package com.fiap.geradorThumbnail.infrastructure.adapter.out.s3;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarThumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class S3SalvarThumbnailsAdapter implements ArmazenarThumbnails {

    private final S3Client s3Client;
    private final String bucketName;

    public S3SalvarThumbnailsAdapter(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public void execute(String caminhoImagem, byte[] imagemBytes) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(caminhoImagem)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(imagemBytes));
        System.out.println("✅ Thumbnail enviada para S3: " + caminhoImagem);
    }
}

