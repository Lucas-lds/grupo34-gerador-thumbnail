resource "aws_instance" "db_client" {
  ami                    = "ami-0ebfd941bbafe70c6"
  instance_type          = "t2.micro"
  key_name               = "chave-hack"
  vpc_security_group_ids = [aws_security_group.ssh_cluster_video2frames.id]
  subnet_id              = "subnet-0ba1d16a81898b46b"

  # Copia o arquivo SQL para a instância
  provisioner "file" {
    source      = "../docker/init.sql"
    destination = "/home/ec2-user/init.sql"

    connection {
      type        = "ssh"
      user        = "ec2-user"
      private_key = tls_private_key.tls_private.private_key_pem
      host        = self.public_ip
    }
  }

  provisioner "remote-exec" {
    inline = [
      "sudo yum update -y", # Instala o cliente MySQL, se necessário
      "sudo wget https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm",
      "sudo dnf install mysql80-community-release-el9-1.noarch.rpm -y",
      "sudo rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023",
      "sudo yum install -y mysql-community-client",
      "mysql -h ${aws_db_instance.mvideo2frames_rds.address} -u ${var.mysql_user} -p${var.mysql_password} -e 'CREATE DATABASE IF NOT EXISTS ${var.mysql_database};'",
      "mysql -h ${aws_db_instance.mvideo2frames_rds.address} -u ${var.mysql_user} -p${var.mysql_password} ${var.mysql_database} < /home/ec2-user/init.sql" # Caminho do arquivo SQL na instância
    ]

    connection {
      type        = "ssh"
      user        = "ec2-user"
      private_key = tls_private_key.tls_private.private_key_pem # Caminho para sua chave privada
      host        = self.public_ip                              # Se a instância for pública
    }
  }

  depends_on = [aws_db_instance.mvideo2frames_rds, aws_key_pair.generated_key]
}