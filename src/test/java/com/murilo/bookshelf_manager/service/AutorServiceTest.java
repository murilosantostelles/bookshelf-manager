package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.autor.AutorRequestDTO;
import com.murilo.bookshelf_manager.dto.autor.AutorResponseDTO;
import com.murilo.bookshelf_manager.entity.Autor;
import com.murilo.bookshelf_manager.exception.BusinessException;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.AutorRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private LivroRepository livroRepository;

    private Autor autor;
    private AutorRequestDTO autorRequestDTO;

    @BeforeEach
    void setUp(){
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("Machado de Assis");

        autorRequestDTO = new AutorRequestDTO("Machado de Assis");
    }

    @Test
    void deveCriarAutor(){
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        AutorResponseDTO autorResponseDTO = autorService.createAutor(autorRequestDTO);

        assertThat(autorResponseDTO).isNotNull();
        assertThat(autorResponseDTO.nome()).isEqualTo("Machado de Assis");
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void deveBuscarAutorPorId(){
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        AutorResponseDTO autorResponseDTO = autorService.findById(1L);

        assertThat(autorResponseDTO).isNotNull();
        assertThat(autorResponseDTO.id()).isEqualTo(1L);
        assertThat(autorResponseDTO.nome()).isEqualTo("Machado de Assis");

        verify(autorRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExceptionQuandoNaoEncontrarAutor(){
        when(autorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorService.findById(2L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Autor não encontrado");

        verify(autorRepository, times(1)).findById(2L);
    }

    @Test
    void deveatualizarAutor(){
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        AutorResponseDTO autorResponseDTO = autorService.updateAutor(1L, autorRequestDTO);

        assertThat(autorResponseDTO).isNotNull();
        assertThat(autorResponseDTO.id()).isEqualTo(1L);
        assertThat(autorResponseDTO.nome()).isEqualTo("Machado de Assis");
        verify(autorRepository, times(1)).findById(1L);
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void deveImpedirExclusaoDeAutorComLivros(){
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(livroRepository.existsByAutorId(1L)).thenReturn(true);

        assertThatThrownBy(() -> autorService.deleteAutor(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Não é possível excluir um autor que possui livros cadastrados");

        verify(autorRepository, never()).delete(any(Autor.class));
    }

    @Test
    void deveDeletarAutor(){
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(livroRepository.existsByAutorId(1L)).thenReturn(false);

        autorService.deleteAutor(1L);

        verify(autorRepository, times(1)).delete(autor);
    }
}
