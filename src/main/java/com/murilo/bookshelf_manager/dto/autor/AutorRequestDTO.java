package com.murilo.bookshelf_manager.dto.autor;

import jakarta.validation.constraints.NotBlank;

public record AutorRequestDTO (
        @NotBlank String nome
){}
