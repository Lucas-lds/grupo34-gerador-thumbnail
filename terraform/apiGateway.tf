# API Gateway REST API
resource "aws_api_gateway_rest_api" "auth_api" {
  name        = "AuthAPI"
  description = "API de autenticação para o video2frames "
}

# Recurso /auth no API Gateway
resource "aws_api_gateway_resource" "auth_resource" {
  rest_api_id = aws_api_gateway_rest_api.auth_api.id
  parent_id   = aws_api_gateway_rest_api.auth_api.root_resource_id
  path_part   = "auth"
}

# Definindo o Authorizer Cognito
resource "aws_api_gateway_authorizer" "cognito_authorizer" {
  name            = "CognitoAuthorizer"
  rest_api_id     = aws_api_gateway_rest_api.auth_api.id
  type            = "COGNITO_USER_POOLS" # Usando Cognito como autorizer
  provider_arns   = [aws_cognito_user_pool.video2frames_user_pool.arn]
  identity_source = "method.request.header.Authorization" # A chave do cabeçalho do JWT
}

# Método POST para o recurso /auth
resource "aws_api_gateway_method" "auth_method" {
  rest_api_id   = aws_api_gateway_rest_api.auth_api.id
  resource_id   = aws_api_gateway_resource.auth_resource.id
  http_method   = "POST"
  authorization = "COGNITO_USER_POOLS" # A autenticação é feita via Cognito
  authorizer_id = aws_api_gateway_authorizer.cognito_authorizer.id
}

# Integração do método POST com a função Lambda para criar usuário
resource "aws_api_gateway_integration" "auth_integration" {
  rest_api_id             = aws_api_gateway_rest_api.auth_api.id
  resource_id             = aws_api_gateway_resource.auth_resource.id
  http_method             = aws_api_gateway_method.auth_method.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = "arn:aws:apigateway:${var.region}:lambda:path/2015-03-31/functions/${aws_lambda_function.create_user_function.arn}/invocations"
}

# Permissão para o API Gateway invocar a Lambda
resource "aws_lambda_permission" "allow_api_gateway" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  principal     = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.create_user_function.function_name
}

# Deployment do API Gateway
resource "aws_api_gateway_deployment" "auth_api_deployment" {
  rest_api_id = aws_api_gateway_rest_api.auth_api.id
  #stage_name  = "prod"  # Isso cria a primeira versão do stage, mas se o stage já existir, ele irá reutilizá-lo

  triggers = {
    redeploy = "${timestamp()}"
  }

  lifecycle {
    create_before_destroy = true # Para garantir que o deployment seja atualizado corretamente
  }

  depends_on = [
    aws_api_gateway_method.auth_method,
    aws_api_gateway_integration.auth_integration
  ]
  # Isso garante que o deployment seja criado após a integração e o método serem criados
  # Isso é importante para evitar erros de dependência
  # ao criar o deployment
}

# Configuração do stage do API Gateway
resource "aws_api_gateway_stage" "auth_api_stage" {
  deployment_id = aws_api_gateway_deployment.auth_api_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.auth_api.id
  stage_name    = "prod"
}