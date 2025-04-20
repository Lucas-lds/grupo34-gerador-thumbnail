variable "cluster_name" {
  type    = string
  default = "video2frames-cluster"
}

variable "region" {
  description = "A região para o provisionamento dos recursos"
  type        = string
  default     = "us-east-1"
}
