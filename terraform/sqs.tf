# Criação da fila de processamento de vídeos
resource "aws_sqs_queue" "sqs-solicitacao-processamento" {
  name                        = "sqs-solicitacao-processamento.fifo"
  fifo_queue                  = true
  content_based_deduplication = true # Ativa a deduplicação baseada no conteúdo
  delay_seconds               = 0
  max_message_size            = 262144 # Tamanho máximo da mensagem em bytes (256 KB)
  message_retention_seconds   = 345600 # Tempo de retenção da mensagem (4 dias)
  receive_wait_time_seconds   = 0
  visibility_timeout_seconds  = 60 # Tempo que a mensagem ficará invisível após ser lida
}
