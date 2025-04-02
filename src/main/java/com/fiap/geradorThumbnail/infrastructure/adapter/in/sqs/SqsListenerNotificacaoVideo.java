package com.fiap.geradorThumbnail.infrastructure.adapter.in.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListenerNotificacaoVideo {

    @SqsListener("sqs-solicitacao-processamento")
    public void listen(String messageBody) {
        System.out.println("📥 Mensagem recebida com sucesso: " + messageBody);

        // TODO: adicionar lógica posteriormente para processamento
    }
}
