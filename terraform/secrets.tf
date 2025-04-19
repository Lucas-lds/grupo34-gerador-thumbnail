resource "kubernetes_secret" "db_secret" {
  metadata {
    name = "db-secret"
  }

  type = "Opaque"

  data = {
    mysql_database = "dmlkZW8yZnJhbWVzREI="             # base64 encoded value
    mysql_user     = "dmlkZW8yZnJhbWVzX3VzZXI="         # base64 encoded value
    mysql_password = "dmlkZW8yZnJhbWVzX3VzZXJfcGFzcw==" # base64 encoded value
    AWS_ACCESS_KEY_ID = "QUtJQTJPQUpUUERHTUtVNkU0T1k="
    AWS_SECRET_ACCESS_KEY = "d1FNSkJzbzlZaE1MZ0N4aTlRRlI2dnhJRDIyaTZBQWhJMU9QQUdIUA=="
  }
}
