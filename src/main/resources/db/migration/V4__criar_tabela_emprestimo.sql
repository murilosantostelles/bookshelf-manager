CREATE TABLE emprestimo (
    id BIGSERIAL PRIMARY KEY,
    nome_pessoa VARCHAR(255),
    data_emprestimo DATE,
    data_devolucao DATE,
    id_livro BIGINT NOT NULL,
    CONSTRAINT fk_emprestimo_livro FOREIGN KEY (id_livro) REFERENCES livro(id)
);