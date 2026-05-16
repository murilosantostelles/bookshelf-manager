package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoRequestDTO;
import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoResponseDTO;
import com.murilo.bookshelf_manager.entity.Emprestimo;
import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.enums.Status;
import com.murilo.bookshelf_manager.repository.EmprestimoRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;

    //post
    @Transactional
    public EmprestimoResponseDTO createEmprestimo(EmprestimoRequestDTO dto){
        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (livro.getStatus() == Status.EMPRESTADO){
            throw new RuntimeException("O Livro "+ livro.getTitulo() + " já está emprestado.");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setNomePessoa(dto.nomePessoa());
        emprestimo.setDataEmprestimo(dto.dataEmprestimo());
        emprestimo.setDataDevolucao(dto.dataDevolucao());
        emprestimo.setLivro(livro);

        livro.setStatus(Status.EMPRESTADO);
        livroRepository.save(livro);
        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);

        return toResponseDTO(emprestimoSalvo);
    }

    //to DTO
    private EmprestimoResponseDTO toResponseDTO(Emprestimo emprestimo){
        return new EmprestimoResponseDTO(
                emprestimo.getId(),
                emprestimo.getNomePessoa(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucao(),
                emprestimo.getLivro().getTitulo()
        );
    }

    //get
    public List<EmprestimoResponseDTO> findAllEmprestimos(){
        return emprestimoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EmprestimoResponseDTO> findByNomePessoa(String nome){
        return emprestimoRepository.findByNomePessoaContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EmprestimoResponseDTO> findByLivroTitulo(String titulo){
        return emprestimoRepository.findByLivroTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EmprestimoResponseDTO> findByDataEmprestimo(LocalDate data){
        return emprestimoRepository.findByDataEmprestimo(data)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public EmprestimoResponseDTO findById(Long id){
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprestimo não encontrado"));

        return toResponseDTO(emprestimo);
    }

    //delete
    @Transactional
    public void deleteEmprestimo(Long id){
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        Livro livro = emprestimo.getLivro();
        livro.setStatus(Status.DISPONIVEL);
        livroRepository.save(livro);
        emprestimoRepository.delete(emprestimo);
    }
}
