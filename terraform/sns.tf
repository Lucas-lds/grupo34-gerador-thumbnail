#O SNS será usado para enviar notificações por e-mail ao usuário em caso de erro.

resource "aws_sns_topic" "video_processing_topic" {
  name = var.sns_topic_name
}

# Assinatura do SNS para enviar e-mail
resource "aws_sns_topic_subscription" "email_subscription" {
  topic_arn = aws_sns_topic.video_processing_topic.arn
  protocol  = "email"
  endpoint  = var.email_address
}


# Fluxo do Processo:

# 1. Aplicação Java Processa o Vídeo: A aplicação Java recebe o vídeo, tenta processá-lo (zipá-lo). Se o processo for bem-sucedido, a aplicação pode prosseguir com o fluxo normal.
# 2. Detecção de Erro: Se a aplicação Java encontrar algum erro durante o processo de zipagem, é publicado uma mensagem no SNS (por meio de uma chamada da AWS SDK).
# 3. SNS Envia o E-mail: O SNS está configurado com um tópico, e um assinante que seja o e-mail do usuário. Quando a aplicação Java publica uma mensagem de erro no SNS, o SNS enviará automaticamente um e-mail para o usuário informando sobre o erro.