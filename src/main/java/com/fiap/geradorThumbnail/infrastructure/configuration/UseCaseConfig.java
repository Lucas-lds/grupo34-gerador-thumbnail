package com.fiap.geradorThumbnail.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fiap.geradorThumbnail.application.port.out.UsuarioServicePortOut;
import com.fiap.geradorThumbnail.core.usecases.UsuarioUseCase;

@Configuration
public class UseCaseConfig {
    
    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioServicePortOut usuarioService) {
        return new com.fiap.geradorThumbnail.application.usecases.UsuarioUseCase(usuarioService);
    }
}
