package com.murilo.bookshelf_manager.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO (
        @NotBlank String nome,
        String subcategoria
){}
