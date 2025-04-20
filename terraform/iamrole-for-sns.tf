# IAM Policy para permitir que o EKS (ou aplicação) publique no SNS
resource "aws_iam_policy" "sns_publish_policy" {
  name        = "sns-publish-policy"
  description = "Permissão para publicar no SNS"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action   = "sns:Publish"
      Effect   = "Allow"
      Resource = aws_sns_topic.video_processing_topic.arn
    }]
  })
}

# Vinculando a política de permissão à IAM Role do EKS
resource "aws_iam_role_policy_attachment" "sns_publish_policy_attachment" {
  policy_arn = aws_iam_policy.sns_publish_policy.arn
  role       = aws_iam_role.eks_node_role.name
}