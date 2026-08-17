package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoRequestDTO;
import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoResponseDTO;
import com.murilo.bookshelf_manager.entity.Emprestimo;
import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.entity.Usuario;
import com.murilo.bookshelf_manager.enums.Status;
import com.murilo.bookshelf_manager.exception.BusinessException;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.EmprestimoRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import com.murilo.bookshelf_manager.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    //post
    @Transactional
    public EmprestimoResponseDTO createEmprestimo(EmprestimoRequestDTO dto){
        Livro livro = livroRepository.findByIdAndUsuario(dto.livroId(), getUsuarioLogado())
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));

        if (livro.getStatus() == Status.EMPRESTADO){
            throw new BusinessException("O Livro "+ livro.getTitulo() + " já está emprestado.");
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

    //get
    public List<EmprestimoResponseDTO> findAllEmprestimos(){
        return emprestimoRepository.findByLivroUsuario(getUsuarioLogado())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EmprestimoResponseDTO> findByNomePessoa(String nome){
        return emprestimoRepository.findByNomePessoaContainingIgnoreCaseAndLivroUsuario(nome, getUsuarioLogado())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EmprestimoResponseDTO> findByLivroTitulo(String titulo){
        return emprestimoRepository.findByLivroTituloContainingIgnoreCaseAndLivroUsuario(titulo, getUsuarioLogado())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EmprestimoResponseDTO> findByDataEmprestimo(LocalDate data){
        return emprestimoRepository.findByDataEmprestimoAndLivroUsuario(data, getUsuarioLogado())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public EmprestimoResponseDTO findById(Long id){
        Emprestimo emprestimo = emprestimoRepository.findByIdAndLivroUsuario(id, getUsuarioLogado())
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado"));

        return toResponseDTO(emprestimo);
    }

    //delete
    @Transactional
    public void deleteEmprestimo(Long id){
        Emprestimo emprestimo = emprestimoRepository.findByIdAndLivroUsuario(id, getUsuarioLogado())
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado"));

        Livro livro = emprestimo.getLivro();
        livro.setStatus(Status.DISPONIVEL);
        livroRepository.save(livro);
        emprestimoRepository.delete(emprestimo);
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
}