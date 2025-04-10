package com.fiap.geradorThumbnail.infrastructure.utils;

public final class UtilityFile {

    private UtilityFile() {
    }

    /**
     * Verifica se o conteúdo enviado é um arquivo ZIP válido com base na assinatura "PK".
     * Lança uma exceção se não for.
     */
    public static void validarFormatoZip(byte[] bytes) {
        boolean isZip = bytes != null &&
                bytes.length >= 4 &&
                bytes[0] == 0x50 && // 'P'
                bytes[1] == 0x4B;   // 'K'

        if (!isZip) {
            throw new IllegalArgumentException("❌ O arquivo enviado não está no formato .zip.");
        }
    }

    /**
     * Remove caracteres não permitidos em nomes de arquivos.
     * Mantém apenas letras, números, hífen, underline e ponto.
     */
    public static String sanitizarNomeArquivo(String texto) {
        if (texto == null) return "";
        return texto.replaceAll("[^a-zA-Z0-9-_.]", "");
    }

    /**
     * Remove a extensão ".zip" do nome do arquivo, se estiver presente no final.
     */
    public static String removerExtensaoZip(String nomeArquivo) {
        if (nomeArquivo == null) return "";
        return nomeArquivo.toLowerCase().endsWith(".zip")
                ? nomeArquivo.substring(0, nomeArquivo.length() - 4)
                : nomeArquivo;
    }

}
