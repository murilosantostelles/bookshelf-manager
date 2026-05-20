CREATE TABLE livro (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    editora VARCHAR(255),
    descricao VARCHAR(255),
    capa_url VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    id_autor BIGINT NOT NULL,
    id_categoria BIGINT NOT NULL,
    CONSTRAINT fk_livro_autor FOREIGN KEY (id_autor) REFERENCES autor(id),
    CONSTRAINT fk_livro_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);