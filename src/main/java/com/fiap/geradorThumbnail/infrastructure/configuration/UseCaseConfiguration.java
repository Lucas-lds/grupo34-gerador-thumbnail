package com.fiap.geradorThumbnail.infrastructure.configuration;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarProcessamento;
import com.fiap.geradorThumbnail.core.usecases.SalvarVideoUseCase;
import com.fiap.geradorThumbnail.application.port.out.ArmazenarVideo;
import com.fiap.geradorThumbnail.application.port.out.EnviarNotificacaoVideo;
import com.fiap.geradorThumbnail.application.service.SalvarVideoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public SalvarVideoUseCase salvarVideoUseCase(ArmazenarVideo armazenarVideo, EnviarNotificacaoVideo enviarNotificacaoVideo, ArmazenarProcessamento armazenarProcessamento) {
        return new SalvarVideoService(armazenarVideo, enviarNotificacaoVideo, armazenarProcessamento);
    }
}
