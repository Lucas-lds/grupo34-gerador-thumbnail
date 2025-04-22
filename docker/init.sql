-- ==============================
-- TABELAS DO BANCO DE DADOS (Refatoradas)
-- ==============================

CREATE TABLE IF NOT EXISTS tb_usuarios (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    cognito_user_id VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL
);

-- Inserir dados iniciais na tabela de usuários
INSERT INTO tb_usuarios (nome, cognito_user_id, email, senha, telefone)
VALUES
('usuario_exemplo', '12345678-1234-1234-1234-123456789012', 'usuario_exemplo@example.com', 'senha123', '11999999999');

CREATE TABLE IF NOT EXISTS tb_processamentos (
    id CHAR(36) PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    nome_video VARCHAR(255) NOT NULL,
    status_processamento VARCHAR(32) NOT NULL,
    criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tb_processamentos_id_usuario
        FOREIGN KEY (id_usuario) REFERENCES tb_usuarios(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
