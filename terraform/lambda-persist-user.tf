resource "aws_lambda_function" "persist_user_lambda" {
  function_name = "PersistUserFunction"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "lambda_function.lambda_handler"
  runtime       = "python3.9"

  filename         = "../lambda-persist-user/lambda_function.zip"
  source_code_hash = filebase64sha256("../lambda-persist-user/lambda_function.zip")

  environment {
    variables = {
      DB_HOST     = aws_db_instance.mvideo2frames_rds.endpoint
      DB_USER     = var.mysql_user
      DB_PASSWORD = var.mysql_password
      DB_NAME     = var.mysql_database
    }
  }

  lifecycle {
    create_before_destroy = true
  }
}