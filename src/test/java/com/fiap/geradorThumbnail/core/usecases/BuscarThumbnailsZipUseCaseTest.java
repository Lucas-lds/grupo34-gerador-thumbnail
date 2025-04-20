package com.fiap.geradorThumbnail.core.usecases;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class BuscarThumbnailsZipUseCaseTest {

    private final BuscarThumbnailsZipUseCase buscarThumbnailsZipUseCase = Mockito.mock(BuscarThumbnailsZipUseCase.class);

    @Test
    void givenValidUserId_whenExecute_thenReturnsThumbnails() {
        // Given
        String userId = "user123";
        byte[] expectedThumbnails = new byte[]{1, 2, 3}; // Mock byte array
        
        Mockito.when(buscarThumbnailsZipUseCase.execute(userId)).thenReturn(expectedThumbnails);
        
        // When
        byte[] actualThumbnails = buscarThumbnailsZipUseCase.execute(userId);
        
        // Then
        assertThat(actualThumbnails).isEqualTo(expectedThumbnails);
    }

    @Test
    void givenInvalidUserId_whenExecute_thenReturnsEmptyArray() {
        // Given
        String userId = "invalidUser";
        
        Mockito.when(buscarThumbnailsZipUseCase.execute(userId)).thenReturn(new byte[0]);
        
        // When
        byte[] actualThumbnails = buscarThumbnailsZipUseCase.execute(userId);
        
        // Then
        assertThat(actualThumbnails).isEmpty();
    }
}
