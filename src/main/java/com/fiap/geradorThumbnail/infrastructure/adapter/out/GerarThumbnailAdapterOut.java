package com.fiap.geradorThumbnail.infrastructure.adapter.out;

import com.fiap.geradorThumbnail.application.port.out.ArmazenarThumbnails;
import com.fiap.geradorThumbnail.application.port.out.BuscarVideo;
import com.fiap.geradorThumbnail.application.port.out.GerarThumbnail;
import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;
import com.fiap.geradorThumbnail.infrastructure.utils.ThumbnailUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class GerarThumbnailAdapterOut implements GerarThumbnail {

    private final BuscarVideo buscarVideo;
    private final ArmazenarThumbnails armazenarThumbnails;

    public GerarThumbnailAdapterOut(BuscarVideo buscarVideo, ArmazenarThumbnails armazenarThumbnails) {
        this.buscarVideo = buscarVideo;
        this.armazenarThumbnails = armazenarThumbnails;
    }

    @Override
    public void execute(ProcessamentoVideo processamentoVideo) {
        String caminhoVideo = processamentoVideo.nomeVideo();
        var arquivoVideo = buscarVideo.execute(caminhoVideo);
        var formatoVideo = processamentoVideo.formatoVideo();

        //TODO mocks de thumbnail substituir pela lógica do Lucas
        InputStream thumb1 = getClass().getResourceAsStream("/mockJpg/thumbnail1.jpg");
        InputStream thumb2 = getClass().getResourceAsStream("/mockJpg/thumbnail2.jpg");
        InputStream thumb3 = getClass().getResourceAsStream("/mockJpg/thumbnail3.jpg");

        Map<String, InputStream> thumbnails = new HashMap<>();
        thumbnails.put(ThumbnailUtils.gerarCaminhoThumbnail(caminhoVideo), thumb1);
        thumbnails.put(ThumbnailUtils.gerarCaminhoThumbnail(caminhoVideo), thumb2);
        thumbnails.put(ThumbnailUtils.gerarCaminhoThumbnail(caminhoVideo), thumb3);

        armazenarThumbnails.execute(thumbnails);
    }
}
