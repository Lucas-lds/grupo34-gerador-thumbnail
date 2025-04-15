package com.fiap.geradorThumbnail.application.port.in;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.sqs.messages.VideoMessage;

public interface ReceberNotificacaoVideo {
    void listen(VideoMessage messageBody);
}
