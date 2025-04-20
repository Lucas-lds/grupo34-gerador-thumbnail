package com.fiap.geradorThumbnail.infrastructure.adapter.out.s3;

import com.fiap.geradorThumbnail.application.port.out.DeletarVideo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Component
public class S3DeletarVideoAdapter implements DeletarVideo {

    private final S3Client s3Client;
    private final String bucketName;

    public S3DeletarVideoAdapter(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public void execute(String caminhoVideo) {

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(caminhoVideo)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

    }
}
