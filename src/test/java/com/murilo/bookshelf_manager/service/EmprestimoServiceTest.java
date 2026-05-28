package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoRequestDTO;
import com.murilo.bookshelf_manager.dto.emprestimo.EmprestimoResponseDTO;
import com.murilo.bookshelf_manager.entity.Emprestimo;
import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.enums.Status;
import com.murilo.bookshelf_manager.exception.BusinessException;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.EmprestimoRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    @InjectMocks
    private EmprestimoService emprestimoService;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private LivroRepository livroRepository;

    private Emprestimo emprestimo;
    private EmprestimoRequestDTO emprestimoRequestDTO;

    private Livro livro;


    @BeforeEach
    void setUp(){
        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Dom Casmurro");
        livro.setStatus(Status.DISPONIVEL);

        emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setNomePessoa("João Silva");
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setLivro(livro);

        emprestimoRequestDTO = new EmprestimoRequestDTO(
                "João Silva",
                 LocalDate.now(),
                null,
                1L
        );
    }

    @Test
    void deveCriarEmprestimo(){
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);

        EmprestimoResponseDTO response = emprestimoService.createEmprestimo(emprestimoRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.nomePessoa()).isEqualTo("João Silva");
        assertThat(response.livroTitulo()).isEqualTo("Dom Casmurro");
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(emprestimoRepository, times(1)).save(any(Emprestimo.class));
    }

    @Test
    void deveImpedirEmprestimoDeLivroIndisponivel(){
        livro.setStatus(Status.EMPRESTADO);
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        assertThatThrownBy(() -> emprestimoService.createEmprestimo(emprestimoRequestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("O Livro Dom Casmurro já está emprestado.");

        verify(emprestimoRepository, never()).save(any(Emprestimo.class));
    }

    @Test
    void deveLancarExceptionQuandoLivroNaoEncontrado(){
        when(livroRepository.findById(2L)).thenReturn(Optional.empty());

        EmprestimoRequestDTO dtoComLivroInexistente = new EmprestimoRequestDTO(
                "João Silva",
                LocalDate.now(),
                null,
                2L
        );

        assertThatThrownBy(() -> emprestimoService.createEmprestimo(dtoComLivroInexistente))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Livro não encontrado");

        verify(emprestimoRepository, never()).save(any(Emprestimo.class));
    }

    @Test
    void deveDeletarEmprestimo(){
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        emprestimoService.deleteEmprestimo(1L);

        assertThat(livro.getStatus()).isEqualTo(Status.DISPONIVEL);
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(emprestimoRepository, times(1)).delete(emprestimo);
    }
}
