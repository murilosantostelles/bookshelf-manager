package com.murilo.bookshelf_manager.controller;

import com.murilo.bookshelf_manager.dto.autor.AutorRequestDTO;
import com.murilo.bookshelf_manager.dto.autor.AutorResponseDTO;
import com.murilo.bookshelf_manager.entity.Autor;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorResponseDTO> createAutor(@Valid @RequestBody AutorRequestDTO dto){
        AutorResponseDTO autorCriado = autorService.createAutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(autorCriado);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> findAllAutores(){
        List<AutorResponseDTO> autoresEncontrados = autorService.findAllAutores();
        return ResponseEntity.ok().body(autoresEncontrados);
    }

    @GetMapping("/nome")
    public ResponseEntity<List<AutorResponseDTO>> findByNome(@RequestParam String nome){
        List<AutorResponseDTO> autoresEncontrados = autorService.findByNome(nome);
        return ResponseEntity.ok().body(autoresEncontrados);

    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> findById(@PathVariable Long id){
        AutorResponseDTO autor = autorService.findById(id);
        return ResponseEntity.ok().body(autor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> updateAutor(@PathVariable Long id, @Valid @RequestBody AutorRequestDTO dto){
        return ResponseEntity.ok().body(autorService.updateAutor(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id){
        autorService.deleteAutor(id);
        return ResponseEntity.noContent().build();
    }
}
