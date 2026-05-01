package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo,Long> {

    List<Emprestimo> findByLivroTituloContainingIgnoreCase(String titulo);

    List<Emprestimo> findByDataEmprestimo(LocalDate dataEmprestimo);

    List<Emprestimo> findByNomePessoaContainingIgnoreCase(String nomePessoa);
}
