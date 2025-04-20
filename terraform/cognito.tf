# Cognito User Pool
resource "aws_cognito_user_pool" "video2frames_user_pool" {
  name = "video2frames-user-pool"

  # Atributos verificados automaticamente
  auto_verified_attributes = ["email"]

  # Política de senhas
  password_policy {
    minimum_length    = 8
    require_lowercase = true
    require_numbers   = true
    require_symbols   = true
    require_uppercase = true
  }

  # Configuração do Lambda pós-confirmação
  lambda_config {
    post_confirmation = aws_lambda_function.persist_user_lambda.arn
  }
}

# Cognito User Pool Client
resource "aws_cognito_user_pool_client" "video2frames_app_client" {
  name         = "video2frames -app-client"
  user_pool_id = aws_cognito_user_pool.video2frames_user_pool.id

  generate_secret = true

  # Fluxos de autenticação
  explicit_auth_flows = [
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH"
  ]

  # Validade dos tokens
  access_token_validity  = 1  # 1 hora
  id_token_validity      = 1  # 1 hora
  refresh_token_validity = 30 # 30 dias
}

# Cognito Identity Pool
resource "aws_cognito_identity_pool" "video2frames_identity_pool" {
  identity_pool_name               = "video2frames-identity-pool"
  allow_unauthenticated_identities = false # Não permitir identidades não autenticadas
  supported_login_providers = {
    "cognito-idp.${var.region}.amazonaws.com/${aws_cognito_user_pool.video2frames_user_pool.id}" = "true"
  }
}

# Criação do usuário no Cognito User Pool
resource "aws_cognito_user" "video2frames_user" {
  user_pool_id = aws_cognito_user_pool.video2frames_user_pool.id
  username     = "usuario_exemplo"
  attributes = {
    email = "usuario_exemplo@example.com"
  }
  temporary_password   = "SenhaTemporaria123!"
  force_alias_creation = false
  message_action       = "SUPPRESS"
}

# Definir a senha permanente para o usuário
resource "null_resource" "set_user_password" {
  provisioner "local-exec" {
    command = <<EOT
      aws cognito-idp admin-set-user-password \
        --user-pool-id ${aws_cognito_user_pool.video2frames_user_pool.id} \
        --username ${aws_cognito_user.video2frames_user.username} \
        --password "SenhaPermanente123!" \
        --permanent
    EOT
  }

  depends_on = [aws_cognito_user.video2frames_user]
}