resource "null_resource" "prepare_code_lambda_persist_user" {
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
      cp /home/raf/Desktop/lambda-persist-user/lambda_function.py ./lambda_package/

      # Cria o arquivo ZIP com o código da Lambda
      cd ./lambda_package && zip -r /home/raf/Desktop/lambda-persist-user/lambda_function.zip .
    EOT
  }

  # Garante que o código seja preparado antes de criar a Lambda
  triggers = {
    code_hash = filesha256("/home/raf/Desktop/lambda-persist-user/lambda_function.zip") # Trigger baseado no hash do código-fonte
  }
}

resource "aws_lambda_function" "persist_user_lambda" {
  filename         = "/home/raf/Desktop/lambda-persist-user/lambda_function.zip"
  function_name    = "PersistUserFunction"
  role             = aws_iam_role.lambda_exec_role.arn
  handler          = "persist_user.lambda_handler"
  runtime          = "python3.9"
  source_code_hash = filebase64sha256("/home/raf/Desktop/lambda-persist-user/lambda_function.zip")

  environment {
    variables = {
      DB_HOST     = aws_db_instance.mvideo2frames_rds.endpoint
      DB_USER     = var.mysql_user
      DB_PASSWORD = var.mysql_password
      DB_NAME     = var.mysql_database
    }
  }
}