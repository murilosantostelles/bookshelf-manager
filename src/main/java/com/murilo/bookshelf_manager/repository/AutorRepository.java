package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Long> {

    List<Autor> findByNomeContainingIgnoreCase(String nome);

}
