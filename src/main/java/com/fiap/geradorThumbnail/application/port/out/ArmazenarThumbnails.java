package com.fiap.geradorThumbnail.application.port.out;

import java.io.InputStream;

import java.util.Map;

public interface ArmazenarThumbnails {
    void execute(Map<String, InputStream> imagens);
}

