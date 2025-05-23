# Define um Role IAM para os nós do EKS
resource "aws_iam_role" "eks_node_role" {
  name = "eks-node-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = ["eks.amazonaws.com", "ec2.amazonaws.com"]
        },
        Action = "sts:AssumeRole",
      },
    ],
  })
}


# Anexa a política ao role dos nós do EKS
resource "aws_iam_role_policy_attachment" "eks_video2frames_node_ecr_policy_attachment" {
  role       = aws_iam_role.eks_node_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy" #aws_iam_policy.eks_video2frames_node_ecr_policy.arn
}

resource "aws_iam_role_policy_attachment" "eks_cni_policy" {
  role       = aws_iam_role.eks_node_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
}

resource "aws_iam_role_policy_attachment" "ec2_container_registry_read_only" {
  role       = aws_iam_role.eks_node_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

resource "aws_iam_policy" "eks_node_policy" {
  name        = "eks-node-policy"
  description = "Policy for EKS nodes"
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "ec2:Describe*",
          "eks:DescribeCluster",
          "ecr:GetAuthorizationToken",
          "autoscaling:DescribeAutoScalingGroups",
          "autoscaling:DescribeLaunchConfigurations",
          "autoscaling:CreateAutoScalingGroup",
          "autoscaling:UpdateAutoScalingGroup",
          "autoscaling:DeleteAutoScalingGroup",
          "iam:*",
          "eks:CreateFargateProfile",
          "eks:CreateNodegroup",
          "eks:DeleteFargateProfile",
          "eks:DeleteNodegroup",
          "eks:ListClusters",
          "eks:ListNodegroups",
          "eks:ListFargateProfiles",
          "ec2:CreateTags",
          "ec2:AssociatePublicIpAddress",
          "elasticnetworking:CreateNetworkInterface",
          "elasticnetworking:DeleteNetworkInterface",
          "elasticnetworking:DescribeNetworkInterfaces",
          "elasticnetworking:DescribeNetworkInterfaceAttributes",
          "elasticloadbalancing:*",
          "ec2:CreateSecurityGroup",
          "ec2:AuthorizeSecurityGroupIngress",
          "ec2:RevokeSecurityGroupIngress",
        ],
        Resource = "*",
      },
      {
        Effect = "Allow",
        Action = [
          "sqs:ReceiveMessage",
          "sqs:DeleteMessage",
          "sqs:GetQueueAttributes"
        ],
        Resource = "*",
      },
      {
        Effect = "Allow",
        Action = [
          "cognito-idp:AdminCreateUser",
          "cognito-idp:AdminSetUserPassword",
          "cognito-idp:AdminGetUser"
        ],
        Resource = "*",
      },      
    ],
  })
}


# Anexa a política ao role dos nós do EKS
resource "aws_iam_role_policy_attachment" "eks_node_policy_attachment" {
  role       = aws_iam_role.eks_node_role.name
  policy_arn = aws_iam_policy.eks_node_policy.arn
}
