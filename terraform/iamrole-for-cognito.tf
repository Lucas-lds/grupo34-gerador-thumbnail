# Role que permite que o Lambda acesse o Cognito
resource "aws_iam_role" "lambda_exec_role" {
  name = "lambda_exec_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}


# Anexa permissões para o Lambda interagir com o Cognito e o banco de dados MySQL
resource "aws_iam_role_policy" "lambda_policy" {
  name = "lambda-cognito-db-policy"
  role = aws_iam_role.lambda_exec_role.id


  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = [
          "cognito-idp:ListUsers",
          "cognito-idp:AdminInitiateAuth",
          "cognito-idp:InitiateAuth",
          "cognito-idp:AdminGetUser",
          "cognito-idp:AdminCreateUser",
          "cognito-idp:AdminSetUserPassword"
        ],
        Effect   = "Allow",
        Resource = "*"
      },
      {
        Action = [
          "rds-data:ExecuteStatement",
          "rds-data:BatchExecuteStatement"
        ],
        Effect   = "Allow",
        Resource = "*"
      }
    ]
  })
}

# resource "aws_iam_role_policy_attachment" "lambda_policy_attachment" {
#   role       = aws_iam_role.lambda_exec_role.name
#   policy_arn = aws_iam_policy.lambda_policy.arn
# }