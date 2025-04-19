output "gateway_url" {
  #value = "https://${aws_api_gateway_rest_api.auth_api.id}.execute-api.${var.region}.amazonaws.com/${aws_api_gateway_deployment.auth_api_deployment.stage_name}"
  value       = "https://${aws_api_gateway_rest_api.auth_api.id}.execute-api.${var.region}.amazonaws.com/${aws_api_gateway_stage.auth_api_stage.stage_name}"
  description = "URL pública do API Gateway"
}

output "cognito_user_pool_id" {
  value       = aws_cognito_user_pool.video2frames_user_pool.id # ID do Cognito User Pool
  description = "ID do Cognito User Pool"
}

### DNS NAME DATA SOURCE ###
#output "URL" {
#  value = length(kubernetes_service.video2frames-api_service.status[0].load_balancer[0].ingress) > 0 ? kubernetes_service.video2frames-api_service.status[0].load_balancer[0].ingress[0].hostname : "Ingress não disponível"
#}


output "cognito_username" {
  value = aws_cognito_user.video2frames_user.username
}

output "cognito_temporary_password" {
  value     = aws_cognito_user.video2frames_user.temporary_password
  sensitive = true
}

output "mvideo2frames_rds_endpoint" {
  value = aws_db_instance.mvideo2frames_rds.endpoint
}

# ARN do tópico SNS que será usado para enviar notificações de erro
output "sns_topic_arn" {
  value = aws_sns_topic.video_processing_topic.arn
}

output "sns_topic_name" {
  value = aws_sns_topic.video_processing_topic.name
}

# COGNITO
output "user_pool_id" {
  value = aws_cognito_user_pool.video2frames_user_pool.id
}

output "user_pool_client_id" {
  value = aws_cognito_user_pool_client.video2frames_app_client.id
}

output "identity_pool_id" {
  value = aws_cognito_identity_pool.video2frames_identity_pool.id
}

output "cognito_issuer_uri" {
  value = "https://cognito-idp.${var.region}.amazonaws.com/${aws_cognito_user_pool.video2frames_user_pool.id}"
}

output "cognito_client_secret" {
  value     = aws_cognito_user_pool_client.video2frames_app_client.client_secret
  sensitive = true
}

# AWS S3
output "s3_bucket_name" {
  value = var.s3_bucket_name
}

output "s3_endpoint" {
  value       = "https://${aws_s3_bucket.video.bucket_regional_domain_name}"
  description = "O endpoint REST para acessar o bucket S3"
}

output "s3_region" {
  value       = var.aws_s3_region
  description = "A região do bucket S3"
}

# AWS SQS

# URL da fila SQS
output "video_queue_url" {
  value = aws_sqs_queue.sqs-solicitacao-processamento.url
}


# AWS Region
# Obtendo a região atual configurada no provider AWS
data "aws_region" "current" {}

output "aws_region" {
  value       = data.aws_region.current.name
  description = "A região configurada no provider AWS"
}