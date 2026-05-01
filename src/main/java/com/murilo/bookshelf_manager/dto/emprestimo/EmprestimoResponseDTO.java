package com.murilo.bookshelf_manager.dto.emprestimo;

import java.time.LocalDate;

public record EmprestimoResponseDTO (
        Long id,
        String nomePessoa,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucao,
        String livroTitulo
){}
