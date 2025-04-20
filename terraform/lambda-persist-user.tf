resource "null_resource" "prepare_code_lambda_persist_user" {
  provisioner "local-exec" {
    command = <<-EOT
      mkdir -p ./lambda_package
      pip install boto3 -t ./lambda_package
      cp ../lambda-persist-user/lambda_function.py ./lambda_package/  # ✅ ALTERADO: caminho relativo
      cd ./lambda_package && zip -r ../lambda-persist-user/lambda_function.zip .  # ✅ ALTERADO: caminho relativo
    EOT
  }

  triggers = {
    code_hash = filesha256("../lambda-persist-user/lambda_function.zip")  # ✅ ALTERADO: caminho relativo
  }
}

resource "aws_lambda_function" "persist_user_lambda" {
  filename         = "../lambda-persist-user/lambda_function.zip"  # ✅ ALTERADO: caminho relativo
  function_name    = "PersistUserFunction"
  role             = aws_iam_role.lambda_exec_role.arn
  handler          = "lambda_function.lambda_handler"
  runtime          = "python3.9"
  source_code_hash = filebase64sha256("../lambda-persist-user/lambda_function.zip")  # ✅ ALTERADO: caminho relativo

  environment {
    variables = {
      DB_HOST     = aws_db_instance.mvideo2frames_rds.endpoint
      DB_USER     = var.mysql_user
      DB_PASSWORD = var.mysql_password
      DB_NAME     = var.mysql_database
    }
  }

  depends_on = [null_resource.prepare_code_lambda_persist_user]

  lifecycle {
    create_before_destroy = true
  }
}