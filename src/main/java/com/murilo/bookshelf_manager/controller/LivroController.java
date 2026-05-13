package com.murilo.bookshelf_manager.controller;

import com.murilo.bookshelf_manager.dto.livro.LivroRequestDTO;
import com.murilo.bookshelf_manager.dto.livro.LivroResponseDTO;
import com.murilo.bookshelf_manager.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDTO> createLivro(@Valid @RequestBody LivroRequestDTO dto){
        LivroResponseDTO livroCriado = livroService.createLivro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroCriado);
    }

    @GetMapping
    public ResponseEntity<Page<LivroResponseDTO>> findAllLivros(Pageable pageable){
        Page<LivroResponseDTO> livrosEncontrdos = livroService.findAllLivros(pageable);
        return ResponseEntity.ok(livrosEncontrdos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(livroService.findLivroById(id));
    }

    @GetMapping("/titulo")
    public  ResponseEntity<Page<LivroResponseDTO>> findByTitulo(@RequestParam String titulo, Pageable pageable){
        Page<LivroResponseDTO> livrosEncontrados = livroService.findByTitulo(titulo,pageable);
        return ResponseEntity.ok(livrosEncontrados);
    }

    @GetMapping("/autor")
    public ResponseEntity<Page<LivroResponseDTO>> findByAutor(@RequestParam String nome, Pageable pageable){
        Page<LivroResponseDTO> livrosEncontrados = livroService.findByAutor(nome,pageable);
        return ResponseEntity.ok(livrosEncontrados);
    }

    @GetMapping("/categoria")
    public ResponseEntity<Page<LivroResponseDTO>> findByCategoria(@RequestParam String nome, Pageable pageable){
        Page<LivroResponseDTO> livrosEncontrados = livroService.findByCategoria(nome, pageable);
        return ResponseEntity.ok(livrosEncontrados);
    }

    @GetMapping("/palavraChave")
    public ResponseEntity<Page<LivroResponseDTO>> findByPalavraChave(@RequestParam String palavra , Pageable pageable){
        Page<LivroResponseDTO> livrosEncontrados = livroService.findByPalavraChave(palavra,pageable);
        return ResponseEntity.ok(livrosEncontrados);
    }

    @GetMapping("/editora")
    public  ResponseEntity<Page<LivroResponseDTO>> finByEditora(@RequestParam String editora, Pageable pageable){
        Page<LivroResponseDTO> livrosEncontrados = livroService.findByEditora(editora,pageable);
        return ResponseEntity.ok(livrosEncontrados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> updateLivro(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO dto){
        return ResponseEntity.ok(livroService.updateLivro(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id){
        livroService.deleteLivro(id);
        return ResponseEntity.noContent().build();
    }
}