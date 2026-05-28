package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LivroRepository extends JpaRepository<Livro,Long> {

        boolean existsByAutorId(Long autorId);

        boolean existsByCategoriaId(Long categoriaId);

        Page<Livro> findByUsuario(Usuario usuario, Pageable pageable);

        Optional<Livro> findByIdAndUsuario(Long id, Usuario usuario);

        Page<Livro> findByTituloContainingIgnoreCaseAndUsuario(String titulo, Usuario usuario, Pageable pageable);

        Page<Livro> findByAutorNomeContainingIgnoreCaseAndUsuario(String nome, Usuario usuario, Pageable pageable);

        Page<Livro> findByCategoriaNomeContainingIgnoreCaseAndUsuario(String nome, Usuario usuario, Pageable pageable);

        Page<Livro> findByEditoraContainingIgnoreCaseAndUsuario(String editora, Usuario usuario, Pageable pageable);

        Page<Livro> findByPalavrasChavePalavraContainingIgnoreCaseAndUsuario(String palavra, Usuario usuario, Pageable pageable);
}
