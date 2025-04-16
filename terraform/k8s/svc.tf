### SERVICE ###
resource "kubernetes_service" "video2frames-api_service" {
  metadata {
    name = "video2frames-service"
  }

  spec {
    type = "LoadBalancer"

    selector = {
      app = "video2frames"
    }

    port {
      protocol    = "TCP"
      port        = 80
      target_port = 8080
    }
  }

  depends_on = [kubernetes_deployment.video2frames-api]
}