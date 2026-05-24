package com.murilo.bookshelf_manager.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO (
    @NotBlank String nome,

    @NotBlank String email,

    @NotBlank
    @Size(min = 8)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()]).{8,}$",
            message = """
        A senha deve ter:
        - pelo menos 8 caracteres
        - uma letra maiúscula
        - um caractere especial
        """
    )
    String senha
){}
