package com.fiap.geradorThumbnail.infrastructure.adapter.out.s3;

import com.fiap.geradorThumbnail.application.port.out.BuscarVideo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;

@Component
public class S3BuscarVideoAdapter implements BuscarVideo {

    private final S3Client s3Client;
    private final String bucketName;

    public S3BuscarVideoAdapter(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public InputStream execute(String caminhoVideo) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(caminhoVideo)
                .build();

        ResponseInputStream<GetObjectResponse> objectStream = s3Client.getObject(getObjectRequest);

        return objectStream;
    }
}
