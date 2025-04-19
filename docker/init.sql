-- ==============================
-- TABELAS DO BANCO DE DADOS (Refatoradas)
-- ==============================

CREATE TABLE IF NOT EXISTS tb_usuarios (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cognito_user_id VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL
);

-- Inserir dados iniciais na tabela de usuários
INSERT INTO tb_usuarios (nome, cognito_user_id, email, senha, telefone)
VALUES
('usuario_exemplo', '12345678-1234-1234-1234-123456789012', 'usuario_exemplo@example.com', 'senha123', '11999999999');

CREATE TABLE IF NOT EXISTS tb_videos (
    id CHAR(36) PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_frames TEXT NOT NULL,
    caminho_video_original TEXT NOT NULL,
    caminho_zip TEXT NOT NULL,
    quantidade_frames INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tb_videos_id_usuario
        FOREIGN KEY (id_usuario) REFERENCES tb_usuarios(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;