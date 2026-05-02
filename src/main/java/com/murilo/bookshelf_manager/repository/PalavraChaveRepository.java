package com.murilo.bookshelf_manager.repository;

import com.murilo.bookshelf_manager.entity.PalavraChave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PalavraChaveRepository extends JpaRepository<PalavraChave,Long> {

    Page<PalavraChave> findByPalavraContainingIgnoreCase(String palavra, Pageable pageable);

    List<PalavraChave> findByLivroId(Long livroId);
}
