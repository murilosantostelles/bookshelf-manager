CREATE TABLE palavra_chave (
    id BIGSERIAL PRIMARY KEY,
    palavra VARCHAR(255) NOT NULL,
    id_livro BIGINT NOT NULL,
    CONSTRAINT fk_palavra_chave_livro FOREIGN KEY (id_livro) REFERENCES livro(id)
);