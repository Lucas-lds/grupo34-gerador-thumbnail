variable "nome_repositorio" {
  type    = string
  default = "video2frames-repositorio"
}

variable "cluster_name" {
  type    = string
  default = "video2frames-cluster"
}

variable "mysql_user" {
  description = "MySQL User"
  type        = string
  default     = "video2frames_user"
}

variable "mysql_password" {
  description = "MySQL Password"
  type        = string
  default     = "video2frames_user_pass"
}

variable "mysql_database" {
  description = "MySQL Database"
  type        = string
  default     = "video2framesDB"
}

variable "region" {
  description = "A região para o provisionamento dos recursos"
  type        = string
  default     = "us-east-1"
}

variable "email_address" {
  type    = string
  default = "r.s.neves@hotmail.com" # Substitua com o e-mail real
}

variable "sns_topic_name" {
  type    = string
  default = "video-processing-topic"
}

# Definição da variável para o nome do bucket
variable "s3_bucket_name" {
  description = "Nome do bucket S3 para armazenar os vídeos"
  type        = string
  default     = "video2frames-storage-bucket"
}

variable "aws_s3_region" {
  description = "A região AWS para o S3"
  type        = string
  default     = "us-east-1"
}