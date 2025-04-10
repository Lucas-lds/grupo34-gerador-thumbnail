package com.fiap.geradorThumbnail.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fiap.geradorThumbnail.application.port.out.UsuarioServicePortOut;
import com.fiap.geradorThumbnail.application.port.out.usecase.UsuarioUseCasePortOut;
import com.fiap.geradorThumbnail.core.usecase.UsuarioUseCase;

@Configuration
public class UseCaseConfig {
    
    @Bean
    public UsuarioUseCasePortOut usuarioUseCase(UsuarioServicePortOut usuarioService) {
        return new UsuarioUseCase(usuarioService);
    }
}
