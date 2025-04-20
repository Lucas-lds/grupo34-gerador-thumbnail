resource "null_resource" "prepare_lambda_code" {
  provisioner "local-exec" {
    command = <<-EOT
      # Exibe o diretório atual para diagnóstico
      echo "Current directory:"
      pwd

      # Exibe o conteúdo dos diretórios relevantes
      echo "Listing contents of terraform directory:"
      ls -l

      # Cria o diretório de pacotes se não existir
      mkdir -p ./lambda_package

      # Instala as dependências necessárias (caso haja alguma)
      pip install boto3 -t ./lambda_package

      # Copia o código-fonte para o diretório de pacotes
      cp /home/raf/Desktop/lambda-criar-usuario/lambda_function.py ./lambda_package/

      # Cria o arquivo ZIP com o código da Lambda
      #cd ./lambda_package && zip -r /home/raf/Desktop/lambda-criar-usuario/lambda_function.zip .
      cd ./lambda_package && zip -r /tmp/lambda_function.zip .

      # Move o arquivo ZIP para o local final
      mv /tmp/lambda_function.zip /home/raf/Desktop/lambda-criar-usuario/lambda_function.zip
    EOT
  }

  # Garante que o código seja preparado antes de criar a Lambda
  triggers = {
    #always_run = "${timestamp()}"
    code_hash = filesha256("/home/raf/Desktop/lambda-criar-usuario/lambda_function.zip") # Trigger baseado no hash do diretório de código
  }
}

resource "aws_lambda_function" "create_user_function" {
  function_name = "CreateUserFunction"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "lambda_function.lambda_handler"
  runtime       = "python3.9"
  filename      = "/home/raf/Desktop/lambda-criar-usuario/lambda_function.zip" # Caminho do arquivo ZIP gerado

  source_code_hash = filebase64sha256("/home/raf/Desktop/lambda-criar-usuario/lambda_function.py") # Hash do arquivo ZIP
  # O hash é usado para detectar alterações no código e forçar a atualização da função Lambda

  environment {
    variables = {
      COGNITO_USER_POOL_ID  = aws_cognito_user_pool.video2frames_user_pool.id
      COGNITO_CLIENT_ID     = aws_cognito_user_pool_client.video2frames_app_client.id
      COGNITO_CLIENT_SECRET = aws_cognito_user_pool_client.video2frames_app_client.client_secret
    }
  }

  timeout = 10 # Aumenta o timeout para 10 segundos

  depends_on = [null_resource.prepare_lambda_code] # Garante que o código seja preparado antes da criação da Lambda

  lifecycle {
    create_before_destroy = true # Garantir que a Lambda seja criada corretamente antes de destruir qualquer outro recurso
  }
}

