package com.fiap.geradorThumbnail.infrastructure.adapter.out.s3;

import com.fiap.geradorThumbnail.core.usecases.BuscarThumbnailsZipUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class S3BuscarThumbnailsZipService implements BuscarThumbnailsZipUseCase {

    private final S3Client s3Client;
    private final String bucketName;

    public S3BuscarThumbnailsZipService(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }


    @Override
    public byte[] execute(String idUsuario) {
        String prefixo = "thumbnails/" + idUsuario + "/";

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefixo)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listRequest);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            for (S3Object obj : response.contents()) {
                String key = obj.key();

                GetObjectRequest getReq = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                try (ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(getReq)) {
                    ZipEntry zipEntry = new ZipEntry(key.replace(prefixo, ""));
                    zipOut.putNextEntry(zipEntry);
                    zipOut.write(inputStream.readAllBytes());
                    zipOut.closeEntry();
                }
            }

            zipOut.finish();
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar ZIP com thumbnails do usuário " + idUsuario, e);
        }
    }
}
