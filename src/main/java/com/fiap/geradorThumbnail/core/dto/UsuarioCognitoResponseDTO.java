package com.fiap.geradorThumbnail.core.dto;

public record UsuarioCognitoResponseDTO(
    boolean success,
    String message,
    String email
) {
    public static UsuarioCognitoResponseDTO success(String message, String email) {
        return new UsuarioCognitoResponseDTO(true, message, email);
    }
    
    public static UsuarioCognitoResponseDTO error(String message, String email) {
        return new UsuarioCognitoResponseDTO(false, message, email);
    }
}
