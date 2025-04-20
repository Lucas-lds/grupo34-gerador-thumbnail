resource "kubernetes_secret" "db_secret" {
  metadata {
    name = "db-secret"
  }

  type = "Opaque"

  data = {
    mysql_database = "dmlkZW8yZnJhbWVzREI="             # base64 encoded value
    mysql_user     = "dmlkZW8yZnJhbWVzX3VzZXI="         # base64 encoded value
    mysql_password = "dmlkZW8yZnJhbWVzX3VzZXJfcGFzcw==" # base64 encoded value
  }
}
