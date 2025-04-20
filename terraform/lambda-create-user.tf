resource "null_resource" "generate_lambda_zip" {
  provisioner "local-exec" {
    command = <<-EOT
      echo "Iniciando empacotamento da Lambda..."

      # Exibe o diretório atual e conteúdo para diagnóstico
      pwd
      ls -l

      # Cria o diretório de pacotes se não existir
      mkdir -p ./lambda_package

      # Instala as dependências necessárias
      pip install boto3 -t ./lambda_package

      # Copia o código-fonte para o diretório de pacotes
      cp /home/raf/Desktop/lambda-criar-usuario/lambda_function.py ./lambda_package/

      # Cria o arquivo ZIP com o código da Lambda
      cd ./lambda_package && zip -r /tmp/lambda_function.zip .

      # Move o arquivo ZIP para o local final
      mv /tmp/lambda_function.zip /home/raf/Desktop/lambda-criar-usuario/lambda_function.zip

      echo "Pacote da Lambda criado com sucesso!"
    EOT
  }

  triggers = {
    always_run = timestamp() # Força execução a cada plano
  }
}

resource "null_resource" "prepare_lambda_code" {
  triggers = {
    code_hash = filesha256("/home/raf/Desktop/lambda-criar-usuario/lambda_function.zip")
  }

  depends_on = [null_resource.generate_lambda_zip]
}

resource "aws_lambda_function" "create_user_function" {
  function_name = "CreateUserFunction"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "lambda_function.lambda_handler"
  runtime       = "python3.9"
  filename      = "/home/raf/Desktop/lambda-criar-usuario/lambda_function.zip"

  # Hash correto do ZIP para detectar mudanças no código
  source_code_hash = filebase64sha256("/home/raf/Desktop/lambda-criar-usuario/lambda_function.zip")

  environment {
    variables = {
      COGNITO_USER_POOL_ID  = aws_cognito_user_pool.video2frames_user_pool.id
      COGNITO_CLIENT_ID     = aws_cognito_user_pool_client.video2frames_app_client.id
      COGNITO_CLIENT_SECRET = aws_cognito_user_pool_client.video2frames_app_client.client_secret
    }
  }

  timeout = 10

  depends_on = [
    null_resource.generate_lambda_zip,
    null_resource.prepare_lambda_code
  ]

  lifecycle {
    create_before_destroy = true
  }
}
