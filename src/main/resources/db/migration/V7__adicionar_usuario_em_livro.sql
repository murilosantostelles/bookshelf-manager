ALTER TABLE livro
ADD COLUMN id_usuario BIGINT,
ADD CONSTRAINT fk_livro_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id);