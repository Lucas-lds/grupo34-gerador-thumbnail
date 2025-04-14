package com.fiap.geradorThumbnail.infrastructure.configuration;

import com.fiap.geradorThumbnail.application.port.out.*;
import com.fiap.geradorThumbnail.application.service.SalvarVideoService;
import com.fiap.geradorThumbnail.application.service.ProcessarVideoService;
import com.fiap.geradorThumbnail.core.usecases.SalvarVideoUseCase;
import com.fiap.geradorThumbnail.core.usecases.ProcessarVideoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public SalvarVideoUseCase salvarVideoUseCase(ArmazenarVideo armazenarVideo, EnviarNotificacaoVideo enviarNotificacaoVideo, ArmazenarProcessamento armazenarProcessamento) {
        return new SalvarVideoService(armazenarVideo, enviarNotificacaoVideo, armazenarProcessamento);
    }

    @Bean
    public ProcessarVideoUseCase processarVideoUseCase(DeletarVideo deletarVideo, AtualizarStatusVideo atualizarStatusVideo, GerarThumbnail gerarThumbnail) {
        return new ProcessarVideoService(deletarVideo, atualizarStatusVideo, gerarThumbnail);
    }
}
