package com.murilo.bookshelf_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class PalavraChave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(nullable = false)
    @NotBlank
    private String palavra;

    @ManyToOne
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;
}
