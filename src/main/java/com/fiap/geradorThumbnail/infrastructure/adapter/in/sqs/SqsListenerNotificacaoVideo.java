package com.fiap.geradorThumbnail.infrastructure.adapter.in.sqs;

import com.fiap.geradorThumbnail.application.port.in.ReceberNotificacaoVideo;
import com.fiap.geradorThumbnail.core.usecases.ProcessarVideoUseCase;
import com.fiap.geradorThumbnail.core.dto.SolicitacaoProcessamentoVideo;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListenerNotificacaoVideo implements ReceberNotificacaoVideo {

    private final ProcessarVideoUseCase processarVideoUseCase;

    public SqsListenerNotificacaoVideo(ProcessarVideoUseCase processarVideoUseCase) {
        this.processarVideoUseCase = processarVideoUseCase;
    }

    @Override
    @SqsListener("sqs-solicitacao-processamento")
    public void listen(SolicitacaoProcessamentoVideo messageBody) {

        System.out.println("📥 Mensagem recebida com sucesso: " + messageBody.videoPath());
        processarVideoUseCase.executar(SolicitacaoProcessamentoVideo.toProcessamentoVideo(messageBody));
        System.out.println("Video processado com sucesso!");

        // TODO tirar classes infra do core ReceberNotificaoVideo e listagem tambem
        // TODO deixar funcoes assyn talvez
    }
}
