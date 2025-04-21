CREATE TABLE tb_processamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    id_cliente VARCHAR(64) NOT NULL,
    nome_video VARCHAR(255) NOT NULL,
    status_processamento VARCHAR(32) NOT NULL,
    criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tb_usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL
);