package com.fiap.geradorThumbnail.core.usecases;

import com.fiap.geradorThumbnail.core.dto.ProcessamentoStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BuscarStatusProcessamentoUseCaseTest {

    private final BuscarStatusProcessamentoUseCase buscarStatusProcessamentoUseCase = Mockito.mock(BuscarStatusProcessamentoUseCase.class);

    @Test
    void testExecute() {
        String idUsuario = "user123";
        List<ProcessamentoStatus> expectedStatusList = List.of(new ProcessamentoStatus(1L, "Video1", "PROCESSANDO", null));
        
        Mockito.when(buscarStatusProcessamentoUseCase.execute(idUsuario)).thenReturn(expectedStatusList);
        
        List<ProcessamentoStatus> actualStatusList = buscarStatusProcessamentoUseCase.execute(idUsuario);
        
        assertThat(actualStatusList).isEqualTo(expectedStatusList);
    }
}
