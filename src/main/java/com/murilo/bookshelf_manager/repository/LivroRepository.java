package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.Autor;
import com.murilo.bookshelf_manager.entity.Categoria;
import com.murilo.bookshelf_manager.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro,Long> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByAutorNomeContainingIgnoreCase(String nome);

    List<Livro> findByCategoriaNomeContainingIgnoreCase(String nome);

    List<Livro> findByEditora(String editora);
}
