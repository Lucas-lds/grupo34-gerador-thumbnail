package com.fiap.geradorThumbnail.infrastructure.adapter.out.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3BuscarThumbnailsZipServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3BuscarThumbnailsZipService service;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setup() {
        service = new S3BuscarThumbnailsZipService(s3Client, bucketName);
    }

    @Test
    void givenUserId_whenThumbnailsExist_thenReturnsZipBytes() throws IOException {
        String userId = "user123";
        String prefix = "thumbnails/" + userId + "/";

        // Mock S3 objects list response
        S3Object obj1 = S3Object.builder().key(prefix + "thumb1.jpg").build();
        S3Object obj2 = S3Object.builder().key(prefix + "thumb2.jpg").build();
        ListObjectsV2Response listResponse = ListObjectsV2Response.builder()
                .contents(obj1, obj2)
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listResponse);

        // Mock S3 getObject responses with dummy content
        byte[] content1 = "content1".getBytes();
        byte[] content2 = "content2".getBytes();

        ResponseInputStream<GetObjectResponse> responseInputStream1 = new ResponseInputStream<>(
                GetObjectResponse.builder().build(),
                new ByteArrayInputStream(content1)
        );
        ResponseInputStream<GetObjectResponse> responseInputStream2 = new ResponseInputStream<>(
                GetObjectResponse.builder().build(),
                new ByteArrayInputStream(content2)
        );

        doAnswer(invocation -> {
            GetObjectRequest req = invocation.getArgument(0);
            if (req.key().equals(prefix + "thumb1.jpg")) {
                return responseInputStream1;
            } else if (req.key().equals(prefix + "thumb2.jpg")) {
                return responseInputStream2;
            }
            return null;
        }).when(s3Client).getObject(any(GetObjectRequest.class));

        // Execute
        byte[] zipBytes = service.execute(userId);

        // Verify listObjectsV2 called with correct bucket and prefix
        ArgumentCaptor<ListObjectsV2Request> listCaptor = ArgumentCaptor.forClass(ListObjectsV2Request.class);
        verify(s3Client).listObjectsV2(listCaptor.capture());
        assertThat(listCaptor.getValue().bucket()).isEqualTo(bucketName);
        assertThat(listCaptor.getValue().prefix()).isEqualTo(prefix);

        // Verify getObject called for each key using ArgumentCaptor
        ArgumentCaptor<GetObjectRequest> getObjectCaptor = ArgumentCaptor.forClass(GetObjectRequest.class);
        verify(s3Client, times(2)).getObject(getObjectCaptor.capture());
        List<GetObjectRequest> capturedRequests = getObjectCaptor.getAllValues();
        assertThat(capturedRequests).extracting(GetObjectRequest::key)
                .containsExactlyInAnyOrder(prefix + "thumb1.jpg", prefix + "thumb2.jpg");

        // Verify the zipBytes contains the two entries with correct content
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry1 = zipInputStream.getNextEntry();
            assertThat(entry1).isNotNull();
            assertThat(entry1.getName()).isEqualTo("thumb1.jpg");
            byte[] buffer1 = zipInputStream.readAllBytes();
            assertThat(buffer1).isEqualTo(content1);
            zipInputStream.closeEntry();

            ZipEntry entry2 = zipInputStream.getNextEntry();
            assertThat(entry2).isNotNull();
            assertThat(entry2.getName()).isEqualTo("thumb2.jpg");
            byte[] buffer2 = zipInputStream.readAllBytes();
            assertThat(buffer2).isEqualTo(content2);
            zipInputStream.closeEntry();

            // No more entries
            assertThat(zipInputStream.getNextEntry()).isNull();
        }
    }

    @Test
    void givenUserId_whenNoThumbnailsExist_thenReturnsEmptyZip() throws IOException {
        String userId = "userEmpty";
        String prefix = "thumbnails/" + userId + "/";

        ListObjectsV2Response listResponse = ListObjectsV2Response.builder()
                .contents(List.of())
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listResponse);

        byte[] zipBytes = service.execute(userId);

        // Verify zipBytes is a valid empty zip
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            assertThat(zipInputStream.getNextEntry()).isNull();
        }
    }

    @Test
    void givenIOException_whenGeneratingZip_thenThrowsRuntimeException() throws IOException {
        String userId = "userError";
        String prefix = "thumbnails/" + userId + "/";

        S3Object obj1 = S3Object.builder().key(prefix + "thumb1.jpg").build();
        ListObjectsV2Response listResponse = ListObjectsV2Response.builder()
                .contents(obj1)
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listResponse);

        // Mock getObject to return a ResponseInputStream that throws IOException on readAllBytes
        ResponseInputStream<GetObjectResponse> faultyStream = mock(ResponseInputStream.class);
        when(faultyStream.readAllBytes()).thenThrow(new IOException("Simulated IO error"));

        doAnswer(invocation -> faultyStream).when(s3Client).getObject(any(GetObjectRequest.class));

        assertThatThrownBy(() -> service.execute(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao gerar ZIP com thumbnails do usuário " + userId);
    }
}
