package com.fiap.geradorThumbnail.infrastructure.exception;

public class UsuarioSemPermissaoCognitoException extends RuntimeException {
    public UsuarioSemPermissaoCognitoException(String message) {
        super(message);
    }
}
