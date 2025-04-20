package com.fiap.geradorThumbnail.infrastructure.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

public class UtilsBDDTest {

    @Nested
    @DisplayName("ThumbnailUtils Tests")
    class ThumbnailUtilsTests {

        @Test
        @DisplayName("extrairUsuario should return user from valid path")
        void extrairUsuario_validPath_returnsUser() {
            String caminho = "videos/user123/video_abc123.mp4";
            String usuario = ThumbnailUtils.extrairUsuario(caminho);
            assertThat(usuario).isEqualTo("user123");
        }

        @Test
        @DisplayName("extrairUsuario should throw exception for invalid path")
        void extrairUsuario_invalidPath_throwsException() {
            String caminho = "invalidpath";
            assertThatThrownBy(() -> ThumbnailUtils.extrairUsuario(caminho))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Caminho de vídeo inválido");
        }

        @Test
        @DisplayName("extrairNomeBase should return base name from valid path")
        void extrairNomeBase_validPath_returnsBaseName() {
            String caminho = "videos/user123/video_abc123.mp4";
            String baseName = ThumbnailUtils.extrairNomeBase(caminho);
            assertThat(baseName).isEqualTo("video");
        }

        @Test
        @DisplayName("extrairNomeBase should throw exception for invalid path")
        void extrairNomeBase_invalidPath_throwsException() {
            String caminho = "videos/user123";
            assertThatThrownBy(() -> ThumbnailUtils.extrairNomeBase(caminho))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Caminho de vídeo inválido");
        }

        @Test
        @DisplayName("extrairNomeBase should throw exception if no UUID in filename")
        void extrairNomeBase_noUUID_throwsException() {
            String caminho = "videos/user123/video.mp4";
            assertThatThrownBy(() -> ThumbnailUtils.extrairNomeBase(caminho))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome do vídeo não contém UUID");
        }

        @Test
        @DisplayName("gerarCaminhoThumbnail should generate valid thumbnail path")
        void gerarCaminhoThumbnail_generatesValidPath() {
            String caminho = "videos/user123/video_abc123.mp4";
            String thumbnailPath = ThumbnailUtils.gerarCaminhoThumbnail(caminho);

            assertThat(thumbnailPath).startsWith("thumbnails/user123/video/video_thumbnail_");
            assertThat(thumbnailPath).endsWith(".jpg");

            // Extract UUID part and validate format
            String[] parts = thumbnailPath.split("_");
            String uuidPart = parts[parts.length - 1].replace(".jpg", "");
            assertThat(uuidPart).matches("[0-9a-fA-F\\-]{36}");
        }
    }

    @Nested
    @DisplayName("UtilityFile Tests")
    class UtilityFileTests {

        @Test
        @DisplayName("validarFormatoZip should not throw for valid ZIP signature")
        void validarFormatoZip_validZip_doesNotThrow() {
            byte[] validZip = {0x50, 0x4B, 0x03, 0x04};
            assertThatCode(() -> UtilityFile.validarFormatoZip(validZip)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("validarFormatoZip should throw for invalid ZIP signature")
        void validarFormatoZip_invalidZip_throwsException() {
            byte[] invalidZip = {0x00, 0x01, 0x02, 0x03};
            assertThatThrownBy(() -> UtilityFile.validarFormatoZip(invalidZip))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não está no formato .zip");
        }

        @Test
        @DisplayName("sanitizarNomeArquivo should remove disallowed characters")
        void sanitizarNomeArquivo_removesDisallowedCharacters() {
            String input = "file@name#with$special%chars!.txt";
            String sanitized = UtilityFile.sanitizarNomeArquivo(input);
            assertThat(sanitized).isEqualTo("filenamewithspecialchars.txt");
        }

        @Test
        @DisplayName("sanitizarNomeArquivo should return empty string for null input")
        void sanitizarNomeArquivo_nullInput_returnsEmpty() {
            String sanitized = UtilityFile.sanitizarNomeArquivo(null);
            assertThat(sanitized).isEmpty();
        }

        @Test
        @DisplayName("removerExtensaoZip should remove .zip extension")
        void removerExtensaoZip_removesZipExtension() {
            String filename = "archive.zip";
            String result = UtilityFile.removerExtensaoZip(filename);
            assertThat(result).isEqualTo("archive");
        }

        @Test
        @DisplayName("removerExtensaoZip should return original if no .zip extension")
        void removerExtensaoZip_noZipExtension_returnsOriginal() {
            String filename = "document.txt";
            String result = UtilityFile.removerExtensaoZip(filename);
            assertThat(result).isEqualTo(filename);
        }

        @Test
        @DisplayName("removerExtensaoZip should return empty string for null input")
        void removerExtensaoZip_nullInput_returnsEmpty() {
            String result = UtilityFile.removerExtensaoZip(null);
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("JsonUtils Tests")
    class JsonUtilsTests {

        static class Dummy {
            public String name;
            public int age;

            public Dummy() {}

            public Dummy(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Dummy dummy = (Dummy) o;
                return age == dummy.age && (name != null ? name.equals(dummy.name) : dummy.name == null);
            }

            @Override
            public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + age;
                return result;
            }
        }

        @Test
        @DisplayName("toJson should convert object to JSON string")
        void toJson_convertsObjectToJson() {
            Dummy dummy = new Dummy("John", 30);
            String json = JsonUtils.toJson(dummy);
            assertThat(json).contains("\"name\":\"John\"");
            assertThat(json).contains("\"age\":30");
        }

        @Test
        @DisplayName("toJson should throw RuntimeException on error")
        void toJson_invalidObject_throwsException() {
            Object invalid = new Object() {
                // ObjectMapper will fail on this because of circular reference or no properties
                public Object self = this;
            };
            assertThatThrownBy(() -> JsonUtils.toJson(invalid))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao converter objeto para JSON");
        }

        @Test
        @DisplayName("fromJson should convert JSON string to object")
        void fromJson_convertsJsonToObject() {
            String json = "{\"name\":\"Jane\",\"age\":25}";
            Dummy dummy = JsonUtils.fromJson(json, Dummy.class);
            assertThat(dummy).isEqualTo(new Dummy("Jane", 25));
        }

        @Test
        @DisplayName("fromJson should throw RuntimeException on error")
        void fromJson_invalidJson_throwsException() {
            String invalidJson = "{name: 'Jane', age: 25"; // malformed JSON
            assertThatThrownBy(() -> JsonUtils.fromJson(invalidJson, Dummy.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao converter JSON para objeto");
        }
    }
}
