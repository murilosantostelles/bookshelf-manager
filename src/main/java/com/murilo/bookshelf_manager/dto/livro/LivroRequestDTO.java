package com.murilo.bookshelf_manager.dto.livro;

import com.murilo.bookshelf_manager.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record LivroRequestDTO (
        @NotBlank String titulo,
        @NotBlank String editora,
        String descricao,
        String capaUrl,
        Status status,
        @NotNull Long autorId,
        @NotNull Long categoriaId,
        List<String> palavrasChave
){}
