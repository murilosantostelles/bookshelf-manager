package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.Emprestimo;
import com.murilo.bookshelf_manager.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByLivroUsuario(Usuario usuario);

    Optional<Emprestimo> findByIdAndLivroUsuario(Long id, Usuario usuario);

    List<Emprestimo> findByNomePessoaContainingIgnoreCaseAndLivroUsuario(String nome, Usuario usuario);

    List<Emprestimo> findByLivroTituloContainingIgnoreCaseAndLivroUsuario(String titulo, Usuario usuario);

    List<Emprestimo> findByDataEmprestimoAndLivroUsuario(LocalDate data, Usuario usuario);
}

