package com.murilo.bookshelf_manager.controller;

import com.murilo.bookshelf_manager.dto.livro.LivroRequestDTO;
import com.murilo.bookshelf_manager.dto.livro.LivroResponseDTO;
import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

}
