# Criação do bucket no S3 para armazenar os vídeos
resource "aws_s3_bucket" "video" {
  #bucket = "${var.s3_bucket_name}-${random_id.bucket_id.hex}" # Nome do bucket, que deve ser único globalmente. O nome é gerado concatenando o nome do bucket definido na variável com um prefixo aleatório.
  bucket = var.s3_bucket_name
  # Garante que o bucket será destruído quando o comando `terraform destroy` for executado
  force_destroy = true

  lifecycle {
    prevent_destroy = false
  }
  
}

resource "random_id" "bucket_id" {
  byte_length = 4 # Gera um ID aleatório de 4 bytes para garantir a unicidade do nome do bucket.

}

# Configuração do versionamento do bucket
resource "aws_s3_bucket_versioning" "video_versioning" {
  bucket = aws_s3_bucket.video.id

  versioning_configuration {
    status = "Enabled" # Habilita o versionamento no bucket, permitindo manter múltiplas versões de objetos.
  }
}

# Configuração do ciclo de vida do bucket
resource "aws_s3_bucket_lifecycle_configuration" "video_lifecycle" {
  bucket = aws_s3_bucket.video.id

  rule {
    id     = "delete-obsolete-files" # Identificador único para a regra de ciclo de vida.
    status = "Enabled"

    filter {
      prefix = "" # Aplica a regra a todos os objetos no bucket.
    }

    expiration {
      days = 7 # Define que os objetos serão excluídos automaticamente após 7 dias.
    }

    noncurrent_version_expiration {
      noncurrent_days = 7 # Define que versões antigas de objetos (não atuais) serão excluídas após 7 dias.
    }
  }
}

resource "aws_s3_bucket_public_access_block" "video_bucket_public_access" {
  bucket = aws_s3_bucket.video.id

  block_public_acls       = true
  block_public_policy     = false # Permite aplicar políticas específicas
  ignore_public_acls      = true
  restrict_public_buckets = false
}