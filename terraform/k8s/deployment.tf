# Buscar o output do RDS diretamente do diretório de infraestrutura
data "terraform_remote_state" "infra" {
  backend = "s3"
  config = {
    bucket = "terraform-state-video2frames"
    key    = "terraform.tfstate"
    region = "us-east-1"
  }
}


# Buscar o segredo Kubernetes existente
data "kubernetes_secret" "db_secret" {
  metadata {
    name      = "db-secret"
  }
}

# Buscar o Service Account existente
data "kubernetes_service_account" "rds_service_account" {
  metadata {
    name      = "rds-service-account"
    namespace = "default"
  }
}

resource "kubernetes_deployment" "video2frames-api" {
  metadata {
    name = "video2frames"
    labels = {
      app = "video2frames"
    }
  }

  spec {
    replicas = 2
    selector {
      match_labels = {
        app = "video2frames"
      }
    }

    template {
      metadata {
        labels = {
          app = "video2frames"
        }
      }

      spec {
        service_account_name = data.kubernetes_service_account.rds_service_account.metadata[0].name

        container {
          name  = "video2frames"
          image = "717279688908.dkr.ecr.us-east-1.amazonaws.com/repositorio:v3" # Substitua pela imagem correta

          # Passando variáveis de ambiente do Cognito
          env {
            name  = "COGNITO_USER_POOL_ID"
            value = data.terraform_remote_state.infra.outputs.cognito_user_pool_id
          }
          env {
            name  = "COGNITO_CLIENT_ID"
            value = data.terraform_remote_state.infra.outputs.user_pool_client_id
          }
          env {
            name  = "COGNITO_CLIENT_SECRET"
            value = data.terraform_remote_state.infra.outputs.cognito_client_secret
          }
          env {
            name  = "COGNITO_IDENTITY_POOL_ID"
            value = data.terraform_remote_state.infra.outputs.identity_pool_id
          }
          env {
            name  = "COGNITO_ISSUER_URI"
            value = data.terraform_remote_state.infra.outputs.cognito_issuer_uri
          }

          # Passando variáveis do S3, SNS e outros
          env {
            name  = "S3_BUCKET_NAME"
            value = data.terraform_remote_state.infra.outputs.s3_bucket_name
          }
          env {
            name  = "S3_ENDPOINT"
            value = data.terraform_remote_state.infra.outputs.s3_endpoint
          }
          env {
            name  = "S3_REGION"
            value = data.terraform_remote_state.infra.outputs.s3_region
          }
          env {
            name  = "SNS_TOPIC_ARN"
            value = data.terraform_remote_state.infra.outputs.sns_topic_arn # ARN do SNS
          }
          env {
            name  = "RDS_ENDPOINT"
            value = data.terraform_remote_state.infra.outputs.mvideo2frames_rds_endpoint # RDS endpoint
          }
          env {
            name  = "SQS_VIDEO_QUEUE_URL"
            value = data.terraform_remote_state.infra.outputs.video_queue_url
          }
          env {
            name  = "REGION"
            value = data.terraform_remote_state.infra.outputs.aws_region
          }
          env {
            name = "AWS_S3_PATH_STYLE"
            value = "true" # Para compatibilidade com o S3
          }
          env {
            name = "MYSQL_DATABASE"
            value_from {
              secret_key_ref {
                name = data.kubernetes_secret.db_secret.metadata[0].name
                key  = "mysql_database"
              }
            }
          }

          # Passando a URL do API Gateway
          env {
            name  = "GATEWAY_URL"
            value = data.terraform_remote_state.infra.outputs.gateway_url
          }

          env {
            name = "MYSQL_USER"
            value_from {
              secret_key_ref {
                name = data.kubernetes_secret.db_secret.metadata[0].name
                key  = "mysql_user"
              }
            }
          }

          env {
            name = "MYSQL_PASSWORD"
            value_from {
              secret_key_ref {
                name = data.kubernetes_secret.db_secret.metadata[0].name
                key  = "mysql_password"
              }
            }
          }

          # Recursos e limites
          resources {
            limits = {
              cpu    = "1"
              memory = "1Gi"
            }
            requests = {
              cpu    = "500m"
              memory = "250Mi"
            }
          }

          # Probes de saúde
          liveness_probe {
            http_get {
              path = "/api/v1/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 120
            period_seconds        = 10
            timeout_seconds       = 5
            failure_threshold     = 3
          }

          port {
            container_port = 8080
          }
        }
      }
    }
  }
}