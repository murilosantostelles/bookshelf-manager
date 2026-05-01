package com.murilo.bookshelf_manager.entity;

import com.murilo.bookshelf_manager.enums.Status;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(nullable = false)
    @NotBlank
    private String titulo;

    @Size(max = 240)
    private String descricao;

    @Size(max = 70)
    private String editora;

    @Column(name = "capa_url")
    private String capaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    //fks
    @ManyToOne
    @JoinColumn(name = "id_autor",nullable = false)
    @NotNull
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "id_categoria",nullable = false)
    @NotNull
    private Categoria categoria;

}
