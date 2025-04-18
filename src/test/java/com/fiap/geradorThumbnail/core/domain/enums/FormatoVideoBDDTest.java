package com.fiap.geradorThumbnail.core.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BDD Tests for FormatoVideo Enum")
public class FormatoVideoBDDTest {

    @Nested
    @DisplayName("getExtensao Method")
    class GetExtensaoTests {

        @Test
        @DisplayName("Should return correct extension for MP4")
        void shouldReturnCorrectExtensionForMP4() {
            assertThat(FormatoVideo.MP4.getExtensao()).isEqualTo("mp4");
        }

        @Test
        @DisplayName("Should return correct extension for MOV")
        void shouldReturnCorrectExtensionForMOV() {
            assertThat(FormatoVideo.MOV.getExtensao()).isEqualTo("mov");
        }

        @Test
        @DisplayName("Should return correct extension for AVI")
        void shouldReturnCorrectExtensionForAVI() {
            assertThat(FormatoVideo.AVI.getExtensao()).isEqualTo("avi");
        }

        @Test
        @DisplayName("Should return correct extension for MKV")
        void shouldReturnCorrectExtensionForMKV() {
            assertThat(FormatoVideo.MKV.getExtensao()).isEqualTo("mkv");
        }

        @Test
        @DisplayName("Should return correct extension for WMV")
        void shouldReturnCorrectExtensionForWMV() {
            assertThat(FormatoVideo.WMV.getExtensao()).isEqualTo("wmv");
        }

        @Test
        @DisplayName("Should return correct extension for FLV")
        void shouldReturnCorrectExtensionForFLV() {
            assertThat(FormatoVideo.FLV.getExtensao()).isEqualTo("flv");
        }
    }

    @Nested
    @DisplayName("fromString Method")
    class FromStringTests {

        @Test
        @DisplayName("Should return MP4 enum for name 'MP4' (case insensitive)")
        void shouldReturnMP4ForName() {
            assertThat(FormatoVideo.fromString("MP4")).isEqualTo(FormatoVideo.MP4);
            assertThat(FormatoVideo.fromString("mp4")).isEqualTo(FormatoVideo.MP4);
            assertThat(FormatoVideo.fromString("Mp4")).isEqualTo(FormatoVideo.MP4);
        }

        @Test
        @DisplayName("Should return MP4 enum for extension 'mp4' (case insensitive)")
        void shouldReturnMP4ForExtension() {
            assertThat(FormatoVideo.fromString("mp4")).isEqualTo(FormatoVideo.MP4);
            assertThat(FormatoVideo.fromString("MP4")).isEqualTo(FormatoVideo.MP4);
            assertThat(FormatoVideo.fromString("Mp4")).isEqualTo(FormatoVideo.MP4);
        }

        @Test
        @DisplayName("Should return MOV enum for name and extension")
        void shouldReturnMOVForNameAndExtension() {
            assertThat(FormatoVideo.fromString("MOV")).isEqualTo(FormatoVideo.MOV);
            assertThat(FormatoVideo.fromString("mov")).isEqualTo(FormatoVideo.MOV);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for unsupported format")
        void shouldThrowExceptionForUnsupportedFormat() {
            assertThatThrownBy(() -> FormatoVideo.fromString("unsupported"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Formato de vídeo não suportado");
        }
    }
}
