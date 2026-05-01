package com.murilo.bookshelf_manager.dto.livro;

import com.murilo.bookshelf_manager.enums.Status;

public record LivroResponseDTO (
        Long id,
        String titulo,
        String editora,
        String descricao,
        String capaUrl,
        Status status,
        String autorNome,
        String categoriaNome
){}
