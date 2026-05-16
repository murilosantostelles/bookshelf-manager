package com.murilo.bookshelf_manager.controller;

import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoRequestDTO;
import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoResponseDTO;
import com.murilo.bookshelf_manager.service.EmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<EmprestimoResponseDTO> createEmprestimo(@Valid @RequestBody EmprestimoRequestDTO dto){
        EmprestimoResponseDTO emprestimoCriado = emprestimoService.createEmprestimo(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoCriado);
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoResponseDTO>> findAllEmprestimos(){
        List<EmprestimoResponseDTO> emprestimos = emprestimoService.findAllEmprestimos();

        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoResponseDTO> findById(@PathVariable Long id){
        EmprestimoResponseDTO emprestimo = emprestimoService.findById(id);
        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping("/nomePessoa")
    public ResponseEntity<List<EmprestimoResponseDTO>> findByNomePessoa(@RequestParam String nome){
        List<EmprestimoResponseDTO> emprestimos = emprestimoService.findByNomePessoa(nome);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/livroTitulo")
    public ResponseEntity<List<EmprestimoResponseDTO>> findByLivroTitulo(@RequestParam String livroTitulo){
        List<EmprestimoResponseDTO> emprestimos = emprestimoService.findByLivroTitulo(livroTitulo);

        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/dataEmprestimo")
    public ResponseEntity<List<EmprestimoResponseDTO>> findByDataEmprestimo(@RequestParam LocalDate dataEmprestimo){
        List<EmprestimoResponseDTO> emprestimos = emprestimoService.findByDataEmprestimo(dataEmprestimo);

        return ResponseEntity.ok(emprestimos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmprestimo(@PathVariable Long id){
        emprestimoService.deleteEmprestimo(id);
        return ResponseEntity.noContent().build();
    }
}
