package com.fiap.geradorThumbnail.infrastructure.adapter.out.s3;

import com.fiap.geradorThumbnail.core.domain.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("BDD Tests for S3 Adapters")
public class S3AdaptersBDDTest {

    private S3Client s3Client;
    private String bucketName;

    private S3SalvarVideoAdapter s3SalvarVideoAdapter;
    private S3SalvarThumbnailsAdapter s3SalvarThumbnailsAdapter;
    private S3BuscarVideoAdapter s3BuscarVideoAdapter;
    private S3DeletarVideoAdapter s3DeletarVideoAdapter;

    @BeforeEach
    void setup() {
        s3Client = mock(S3Client.class, withSettings().defaultAnswer((Answer) invocation -> {
            // Default answer to avoid ambiguity errors
            return null;
        }));
        bucketName = "test-bucket";

        s3SalvarVideoAdapter = new S3SalvarVideoAdapter(s3Client, bucketName);
        s3SalvarThumbnailsAdapter = new S3SalvarThumbnailsAdapter(s3Client, bucketName);
        s3BuscarVideoAdapter = new S3BuscarVideoAdapter(s3Client, bucketName);
        s3DeletarVideoAdapter = new S3DeletarVideoAdapter(s3Client, bucketName);
    }

    @Nested
    @DisplayName("S3SalvarVideoAdapter Tests")
    class S3SalvarVideoAdapterTests {

        @Test
        @DisplayName("Should store video successfully")
        void shouldStoreVideoSuccessfully() {
            Video video = new Video("video data".getBytes(StandardCharsets.UTF_8), "user123", "mp4", "video.mp4");

            assertThatCode(() -> s3SalvarVideoAdapter.execute(List.of(video)))
                    .doesNotThrowAnyException();

            verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        @DisplayName("Should throw exception when storing video fails")
        void shouldThrowExceptionWhenStoringVideoFails() {
            Video video = new Video("video data".getBytes(StandardCharsets.UTF_8), "user123", "mp4", "video.mp4");
            doThrow(new RuntimeException("Storage failure"))
                    .when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

            assertThatThrownBy(() -> s3SalvarVideoAdapter.execute(List.of(video)))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Storage failure");
        }
    }

    @Nested
    @DisplayName("S3SalvarThumbnailsAdapter Tests")
    class S3SalvarThumbnailsAdapterTests {

        @Test
        @DisplayName("Should store thumbnail successfully")
        void shouldStoreThumbnailSuccessfully() {
            String path = "thumbnail.jpg";
            byte[] image = "image data".getBytes(StandardCharsets.UTF_8);

            assertThatCode(() -> s3SalvarThumbnailsAdapter.execute(path, image))
                    .doesNotThrowAnyException();

            verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        @DisplayName("Should throw exception when storing thumbnail fails")
        void shouldThrowExceptionWhenStoringThumbnailFails() {
            String path = "thumbnail.jpg";
            byte[] image = "image data".getBytes(StandardCharsets.UTF_8);
            doThrow(new RuntimeException("Storage failure"))
                    .when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

            assertThatThrownBy(() -> s3SalvarThumbnailsAdapter.execute(path, image))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Storage failure");
        }
    }

    @Nested
    @DisplayName("S3BuscarVideoAdapter Tests")
    class S3BuscarVideoAdapterTests {

        @Test
        @DisplayName("Should return InputStream for existing video")
        void shouldReturnInputStreamForExistingVideo() throws Exception {
            String videoPath = "video.mp4";
            byte[] videoData = "video data".getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(videoData);

            ResponseInputStream<GetObjectResponse> responseInputStream = mock(ResponseInputStream.class);
            when(responseInputStream.readAllBytes()).thenReturn(videoData);

            when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(responseInputStream);

            InputStream result = s3BuscarVideoAdapter.execute(videoPath);

            assertThat(result).isNotNull();
            byte[] resultBytes = result.readAllBytes();
            assertThat(resultBytes).isEqualTo(videoData);

            verify(s3Client, times(1)).getObject(any(GetObjectRequest.class));
        }

        @Test
        @DisplayName("Should throw exception when video not found")
        void shouldThrowExceptionWhenVideoNotFound() {
            String videoPath = "nonexistent.mp4";
            when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(new RuntimeException("Video not found"));

            assertThatThrownBy(() -> s3BuscarVideoAdapter.execute(videoPath))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Video not found");
        }
    }

    @Nested
    @DisplayName("S3DeletarVideoAdapter Tests")
    class S3DeletarVideoAdapterTests {

        @Test
        @DisplayName("Should delete video successfully")
        void shouldDeleteVideoSuccessfully() {
            String videoPath = "video.mp4";

            assertThatCode(() -> s3DeletarVideoAdapter.execute(videoPath))
                    .doesNotThrowAnyException();

            verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
        }

        @Test
        @DisplayName("Should throw exception when deleting video fails")
        void shouldThrowExceptionWhenDeletingVideoFails() {
            String videoPath = "video.mp4";
            doThrow(new RuntimeException("Delete failure"))
                    .when(s3Client).deleteObject(any(DeleteObjectRequest.class));

            assertThatThrownBy(() -> s3DeletarVideoAdapter.execute(videoPath))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Delete failure");
        }
    }
}
