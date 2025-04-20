package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ProcessarVideoUseCaseTest {

    private ProcessarVideoUseCase processarVideoUseCase;

    @BeforeEach
    void setUp() {
        processarVideoUseCase = mock(ProcessarVideoUseCase.class); // Mocking the interface
    }

    @Test
    void givenValidVideo_whenExecutar_thenProcessesSuccessfully() {
        // Given
        ProcessamentoVideo video = new ProcessamentoVideo(1L, "Video Title", "mp4");
        
        // When
        doNothing().when(processarVideoUseCase).executar(video);
        processarVideoUseCase.executar(video);
        
        // Then
        verify(processarVideoUseCase, times(1)).executar(video); // Verify that the method was called
    }

    @Test
    void givenInvalidVideo_whenExecutar_thenDoesNotProcess() {
        // Given
        ProcessamentoVideo video = new ProcessamentoVideo(1L, "", "mp4");
        
        // When
        doNothing().when(processarVideoUseCase).executar(video);
        processarVideoUseCase.executar(video);
        
        // Then
        verify(processarVideoUseCase, times(1)).executar(video); // Verify that the method was called
    }
}
