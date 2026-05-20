CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    subcategoria VARCHAR(255)
);