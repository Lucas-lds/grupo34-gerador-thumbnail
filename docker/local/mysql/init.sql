CREATE TABLE IF NOT EXISTS processamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente VARCHAR(64) NOT NULL,
    nome_video VARCHAR(255) NOT NULL,
    status_processamento VARCHAR(32) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tb_usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_videos (
    id CHAR(36) PRIMARY KEY,
    id_usuario INT NOT NULL,
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