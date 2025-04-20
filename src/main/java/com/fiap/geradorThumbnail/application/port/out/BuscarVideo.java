package com.fiap.geradorThumbnail.application.port.out;

import java.io.InputStream;

public interface BuscarVideo {

    InputStream execute(String caminhoVideo);
}
