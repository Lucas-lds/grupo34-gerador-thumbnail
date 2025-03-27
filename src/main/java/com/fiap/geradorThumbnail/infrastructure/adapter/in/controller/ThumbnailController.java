package com.fiap.geradorThumbnail.infrastructure.adapter.in.controller;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.SqsPublisherProcessamento;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
public class ThumbnailController {

    private final SqsPublisherProcessamento publisher;

    public ThumbnailController(SqsPublisherProcessamento publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/enviar")
    public String enviarMensagem(@RequestBody String mensagem) {
        publisher.sendMessage(mensagem);
        return "Mensagem enviada!";
    }
}
