# Define o Role IAM para acesso ao RDS
resource "aws_iam_role" "rds_access_role" {
  name = "rds-access-role"

    assume_role_policy = jsonencode({
      Version = "2012-10-17",
      Statement = [
        {
          Effect = "Allow",
          Principal = {
            Federated = aws_iam_openid_connect_provider.eks_oidc_provider.arn
          },
          Action = "sts:AssumeRoleWithWebIdentity",
          Condition = {
            StringEquals = {
              "${replace(aws_eks_cluster.eks-cluster.identity[0].oidc[0].issuer, "https://", "")}:sub" = "system:serviceaccount:default:rds-service-account"
            }
          }
        }
      ]
    })
}
#   assume_role_policy = jsonencode({
#     Version = "2012-10-17",
#     Statement = [
#       {
#         Effect = "Allow",
#         Principal = {
#           Service = [
#             "lambda.amazonaws.com",
#             "eks.amazonaws.com",
#           ]
#         },
#         Action = "sts:AssumeRole",
#       },
#     ],
#   })
# }

# Define a política de IAM para acesso ao RDS
resource "aws_iam_policy" "rds_access_policy" {
  name        = "rds-access-policy"
  description = "Policy for accessing RDS"
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "rds:DescribeDBInstances",
          "rds:Connect",
        ],
        Resource = "*",
      },
    ],
  })
}

# Anexa a política ao Role
resource "aws_iam_role_policy_attachment" "rds_access_policy_attachment" {
  role       = aws_iam_role.rds_access_role.name
  policy_arn = aws_iam_policy.rds_access_policy.arn
}

# Cria a Service Account no Kubernetes
resource "kubernetes_service_account" "rds_service_account" {
  metadata {
    name      = "rds-service-account"
    namespace = "default"
    annotations = {
      "eks.amazonaws.com/role-arn" = aws_iam_role.rds_access_role.arn
    }
  }
}