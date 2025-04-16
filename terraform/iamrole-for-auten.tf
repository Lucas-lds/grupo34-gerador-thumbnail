# IAM Role que o usuário autenticado possa usar para acessar os recursos protegidos.
resource "aws_iam_role" "video2frames_authenticated_role" {
  name = "video2frames_authenticated_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRoleWithWebIdentity"
        Effect = "Allow"
        Principal = {
          Federated = "cognito-identity.amazonaws.com"
        }
        Condition = {
          StringEquals = {
            "cognito-identity.amazonaws.com:aud" = aws_cognito_identity_pool.video2frames_identity_pool.id
          }
        }
      }
    ]
  })
}

# Política para usuários autenticados acessarem os recursos da AWS
resource "aws_iam_policy" "video2frames_authenticated_policy" {
  name        = "video2frames_authenticated_policy"
  description = "Política para usuários autenticados acessarem os recursos da AWS"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action   = "s3:GetObject"
        Effect   = "Allow"
        Resource = "arn:aws:s3:::your-bucket-name/*"
      },
    ]
  })
}

# Anexar a política ao role
resource "aws_iam_role_policy_attachment" "video2frames_authenticated_policy_attachment" {
  policy_arn = aws_iam_policy.video2frames_authenticated_policy.arn
  role       = aws_iam_role.video2frames_authenticated_role.name
}

# Associar o role de autenticação ao Identity Pool
resource "aws_cognito_identity_pool_roles_attachment" "video2frames_identity_pool_role_attachment" {
  identity_pool_id = aws_cognito_identity_pool.video2frames_identity_pool.id
  roles = {
    "authenticated" = aws_iam_role.video2frames_authenticated_role.arn
  }
}

# Este IAM Role atribui permissões para que usuários autenticados possam acessar recursos específicos, como um bucket S3 ou uma tabela DynamoDB.