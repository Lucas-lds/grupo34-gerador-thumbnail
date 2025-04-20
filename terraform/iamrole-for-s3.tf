resource "aws_s3_bucket_policy" "video_bucket_policy" {
  bucket = aws_s3_bucket.video.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          AWS = [
            "arn:aws:iam::717279688908:role/${aws_iam_role.eks_role.name}",
            "arn:aws:iam::717279688908:user/hacka"
          ]
        },
        Action = [
          "s3:PutObject",
          "s3:PutObjectAcl",
          "s3:GetObject",
          "s3:ListBucket"
        ],
        Resource = [
          "${aws_s3_bucket.video.arn}",
          "${aws_s3_bucket.video.arn}/*"
        ]
      }
    ]
  })
}