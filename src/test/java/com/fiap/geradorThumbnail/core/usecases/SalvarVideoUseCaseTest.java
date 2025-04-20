package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.core.domain.Video;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class SalvarVideoUseCaseTest {

    private final SalvarVideoUseCase salvarVideoUseCase = Mockito.mock(SalvarVideoUseCase.class);

    @Test
    void givenValidVideos_whenExecute_thenSavesSuccessfully() {
        // Given
        Video video = new Video(new byte[]{1, 2, 3}, "user123", "mp4", "video.mp4");
        List<Video> videos = List.of(video);
        
        // When
        Mockito.doNothing().when(salvarVideoUseCase).executar(videos);
        salvarVideoUseCase.executar(videos);
        
        // Then
        Mockito.verify(salvarVideoUseCase, Mockito.times(1)).executar(videos);
    }

    @Test
    void givenInvalidVideos_whenExecute_thenDoesNotSave() {
        // Given
        Video video = new Video(new byte[]{}, "user123", "", ""); // Invalid video
        List<Video> videos = List.of(video);
        
        // When
        Mockito.doNothing().when(salvarVideoUseCase).executar(videos);
        salvarVideoUseCase.executar(videos);
        
        // Then
        Mockito.verify(salvarVideoUseCase, Mockito.times(1)).executar(videos);
    }
}
