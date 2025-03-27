package com.fiap.geradorThumbnail.infrastructure.adapter.in.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListenerProcessamento {

    @SqsListener("sqs-solicitacao-processamento")
    public void listen(String messageBody) {
        System.out.println("📥 Mensagem recebida: " + messageBody);

        // TODO: adicionar lógica posteriormente
    }
}
