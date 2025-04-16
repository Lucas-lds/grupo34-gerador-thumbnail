package com.fiap.geradorThumbnail.core.dto;

public record UsuarioCognitoResponse(
    boolean success,
    String message,
    String email
) {
    public static UsuarioCognitoResponse success(String message, String email) {
        return new UsuarioCognitoResponse(true, message, email);
    }
    
    public static UsuarioCognitoResponse error(String message, String email) {
        return new UsuarioCognitoResponse(false, message, email);
    }
}
