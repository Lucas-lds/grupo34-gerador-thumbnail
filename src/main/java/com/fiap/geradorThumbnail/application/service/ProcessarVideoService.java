package com.fiap.geradorThumbnail.application.service;

import com.fiap.geradorThumbnail.application.port.out.AtualizarStatusVideo;
import com.fiap.geradorThumbnail.application.port.out.DeletarVideo;
import com.fiap.geradorThumbnail.application.port.out.GerarThumbnail;
import com.fiap.geradorThumbnail.core.domain.enums.StatusProcessamento;
import com.fiap.geradorThumbnail.core.dto.ProcessamentoVideo;
import com.fiap.geradorThumbnail.core.usecases.ProcessarVideoUseCase;

public class ProcessarVideoService implements ProcessarVideoUseCase {

    private final DeletarVideo deletarVideo;
    private final AtualizarStatusVideo atualizarStatusVideo;
    private final GerarThumbnail gerarThumbnail;

    public ProcessarVideoService(DeletarVideo deletarVideo, AtualizarStatusVideo atualizarStatusVideo, GerarThumbnail gerarThumbnail) {
        this.deletarVideo = deletarVideo;
        this.atualizarStatusVideo = atualizarStatusVideo;
        this.gerarThumbnail = gerarThumbnail;
    }

    @Override
    public void executar(ProcessamentoVideo processamentoVideo) {
        try {
            atualizarStatusVideo.execute(StatusProcessamento.PROCESSANDO, processamentoVideo.idProcessamento());
            gerarThumbnail.execute(processamentoVideo);
            atualizarStatusVideo.execute(StatusProcessamento.FINALIZADO, processamentoVideo.idProcessamento());
        } catch (Exception e) {
            atualizarStatusVideo.execute(StatusProcessamento.ERRO, processamentoVideo.idProcessamento());
            throw e;
        } finally {
            deletarVideo.execute(processamentoVideo.nomeVideo());
        }
    }

}
