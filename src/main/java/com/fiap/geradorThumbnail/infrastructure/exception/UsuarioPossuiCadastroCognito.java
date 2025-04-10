package com.fiap.geradorThumbnail.infrastructure.exception;

public class UsuarioPossuiCadastroCognito extends RuntimeException {
    private final String email;

    public UsuarioPossuiCadastroCognito(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
