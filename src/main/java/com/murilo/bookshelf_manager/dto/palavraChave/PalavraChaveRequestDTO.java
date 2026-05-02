package com.murilo.bookshelf_manager.dto.palavraChave;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PalavraChaveRequestDTO(
        @NotBlank String palavra,
        @NotNull Long livroId
) {}
