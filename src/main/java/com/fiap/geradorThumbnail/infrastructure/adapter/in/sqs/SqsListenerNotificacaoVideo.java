package com.fiap.geradorThumbnail.infrastructure.adapter.in.sqs;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.messages.VideoMessage;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListenerNotificacaoVideo {

    @SqsListener("sqs-solicitacao-processamento")
    public void listen(VideoMessage messageBody) {

        System.out.println("📥 Mensagem recebida com sucesso: " + messageBody.videoPath());

        // TODO: adicionar lógica posteriormente para processamento
    }
}
