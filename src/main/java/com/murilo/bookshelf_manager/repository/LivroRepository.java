package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LivroRepository extends JpaRepository<Livro,Long> {

        Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

        Page<Livro> findByAutorNomeContainingIgnoreCase(String nome, Pageable pageable);

        Page<Livro> findByCategoriaNomeContainingIgnoreCase(String nome, Pageable pageable);

        Page<Livro> findByEditoraContainingIgnoreCase(String editora, Pageable pageable);


}
