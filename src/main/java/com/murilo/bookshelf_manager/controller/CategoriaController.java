package com.murilo.bookshelf_manager.controller;

import com.murilo.bookshelf_manager.dto.categoria.CategoriaRequestDTO;
import com.murilo.bookshelf_manager.dto.categoria.CategoriaResponseDTO;
import com.murilo.bookshelf_manager.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> createCategoria(@Valid @RequestBody CategoriaRequestDTO dto){
        CategoriaResponseDTO categoriaCriada = categoriaService.createCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCriada);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> findAllCategorias(){
        List<CategoriaResponseDTO> categorias = categoriaService.findAllCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Long id ){
        CategoriaResponseDTO categoria = categoriaService.findById(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping ("/nome")
    public ResponseEntity<List<CategoriaResponseDTO>> findByNome(@RequestParam String nome){
        List<CategoriaResponseDTO> categorias = categoriaService.findByNome(nome);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping ("/subcategoria")
    public ResponseEntity<List<CategoriaResponseDTO>> findBySubcategoria(@RequestParam String subcategoria){
        List<CategoriaResponseDTO> categorias = categoriaService.findBySubcategoria(subcategoria);
        return ResponseEntity.ok(categorias);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> updateCategoria(@PathVariable Long id ,@Valid @RequestBody CategoriaRequestDTO dto){
        CategoriaResponseDTO categoriaAtualizada = categoriaService.updateCategoria(id, dto);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id){
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
