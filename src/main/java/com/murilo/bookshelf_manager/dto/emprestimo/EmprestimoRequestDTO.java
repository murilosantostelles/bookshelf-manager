package com.murilo.bookshelf_manager.dto.emprestimo;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmprestimoRequestDTO (
        String nomePessoa,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucao,
        @NotNull Long livroId
){}
