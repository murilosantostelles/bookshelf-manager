package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    List<Categoria> findByNomeContainingIgnoreCase(String nome);
    List<Categoria> findBySubcategoriaContainingIgnoreCase(String subcategoria);
}
