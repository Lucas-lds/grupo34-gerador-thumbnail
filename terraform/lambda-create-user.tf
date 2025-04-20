resource "aws_lambda_function" "create_user_function" {
  function_name = "CreateUserFunction"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "lambda_function.lambda_handler"
  runtime       = "python3.9"

  filename         = "../lambda-create-user/lambda_function.zip"
  source_code_hash = filebase64sha256("../lambda-create-user/lambda_function.zip")

  environment {
    variables = {
      COGNITO_USER_POOL_ID  = aws_cognito_user_pool.video2frames_user_pool.id
      COGNITO_CLIENT_ID     = aws_cognito_user_pool_client.video2frames_app_client.id
      COGNITO_CLIENT_SECRET = aws_cognito_user_pool_client.video2frames_app_client.client_secret
    }
  }

  timeout = 10

  lifecycle {
    create_before_destroy = true
  }
}