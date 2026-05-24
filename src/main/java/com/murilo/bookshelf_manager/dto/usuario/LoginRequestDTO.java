package com.murilo.bookshelf_manager.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String email,
        @NotBlank String senha
) {
}
