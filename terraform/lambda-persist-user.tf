resource "null_resource" "generate_lambda_zip_persist_user" {
  provisioner "local-exec" {
    command = <<-EOT
      echo "Iniciando empacotamento da Lambda PersistUser..."

      mkdir -p ./lambda_package
      pip install boto3 -t ./lambda_package
      cp /home/raf/Desktop/lambda-persist-user/lambda_function.py ./lambda_package/
      cd ./lambda_package && zip -r /tmp/lambda_function.zip .
      mv /tmp/lambda_function.zip /home/raf/Desktop/lambda-persist-user/lambda_function.zip

      echo "Pacote da Lambda PersistUser criado com sucesso!"
    EOT
  }

  triggers = {
    always_run = timestamp()
  }
}

resource "null_resource" "prepare_code_lambda_persist_user" {
  triggers = {
    code_hash = filesha256("/home/raf/Desktop/lambda-persist-user/lambda_function.zip")
  }

  depends_on = [null_resource.generate_lambda_zip_persist_user]
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

  depends_on = [
    null_resource.generate_lambda_zip_persist_user,
    null_resource.prepare_code_lambda_persist_user
  ]
}
