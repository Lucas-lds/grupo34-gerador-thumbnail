package com.fiap.geradorThumbnail.infrastructure.adapter.in.sqs;

import com.fiap.geradorThumbnail.core.usecases.ProcessarVideoUseCase;
import com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.messages.VideoMessage;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListenerNotificacaoVideo {

    private final ProcessarVideoUseCase processarVideoUseCase;

    public SqsListenerNotificacaoVideo(ProcessarVideoUseCase processarVideoUseCase) {
        this.processarVideoUseCase = processarVideoUseCase;
    }

    @SqsListener("sqs-solicitacao-processamento")
    public void listen(VideoMessage messageBody) {

        System.out.println("📥 Mensagem recebida com sucesso: " + messageBody.videoPath());
        processarVideoUseCase.executar(VideoMessage.toProcessamentoVideo(messageBody));
        System.out.println("Video processado com sucesso!");

        // TODO rotar get passando como parametro idProcessamento e nome video para baixar as thumbs no s3 dentro de um zip
        // TODO rota get pra consultar os processamentos do MySql
        // TODO deixar funcoes assyn talvez
    }
}
