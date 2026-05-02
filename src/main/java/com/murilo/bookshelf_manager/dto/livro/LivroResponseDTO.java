package com.murilo.bookshelf_manager.dto.livro;

import com.murilo.bookshelf_manager.enums.Status;

import java.util.List;

public record LivroResponseDTO (
        Long id,
        String titulo,
        String editora,
        String descricao,
        String capaUrl,
        Status status,
        String autorNome,
        String categoriaNome,
        List<String> palavrasChave
){}
