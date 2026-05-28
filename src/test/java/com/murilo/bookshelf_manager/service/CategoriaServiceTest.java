package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.categoria.CategoriaRequestDTO;
import com.murilo.bookshelf_manager.dto.categoria.CategoriaResponseDTO;
import com.murilo.bookshelf_manager.entity.Categoria;
import com.murilo.bookshelf_manager.exception.BusinessException;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.CategoriaRepository;
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
public class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private LivroRepository livroRepository;

    private Categoria categoria;
    private CategoriaRequestDTO categoriaRequestDTO;

    @BeforeEach
    void setUp(){
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Psicologia");
        categoria.setSubcategoria("Filosofia");

        categoriaRequestDTO = new CategoriaRequestDTO("Psicologia", "Filosofia");
    }

    @Test
    void deveCriarCategoria(){
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaResponseDTO categoriaResponseDTO = categoriaService.createCategoria(categoriaRequestDTO);

        assertThat(categoriaResponseDTO).isNotNull();
        assertThat(categoriaResponseDTO.nome()).isEqualTo("Psicologia");
        assertThat(categoriaResponseDTO.subcategoria()).isEqualTo("Filosofia");
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void deveBuscarCategoriaPorId(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaResponseDTO categoriaResponseDTO = categoriaService.findById(1L);

        assertThat(categoriaResponseDTO).isNotNull();
        assertThat(categoriaResponseDTO.id()).isEqualTo(1L);
        assertThat(categoriaResponseDTO.nome()).isEqualTo("Psicologia");
        assertThat(categoriaResponseDTO.subcategoria()).isEqualTo("Filosofia");

        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExceptionQuandoCategoriaNaoEncontrada(){
        when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.findById(2L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Categoria não encontrada");

        verify(categoriaRepository, times(1)).findById(2L);
    }

    @Test
    void deveAtualizarCategoria(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaResponseDTO categoriaResponseDTO = categoriaService.updateCategoria(1L, categoriaRequestDTO);

        assertThat(categoriaResponseDTO).isNotNull();
        assertThat(categoriaResponseDTO.id()).isEqualTo(1L);
        assertThat(categoriaResponseDTO.nome()).isEqualTo("Psicologia");
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void deveImpedirExclusaoDeCategoriaComLivros(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(livroRepository.existsByCategoriaId(1L)).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.deleteCategoria(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Não é possível excluir uma categoria que possui livros cadastrados");

        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }

    @Test
    void deveDeletarCategoria(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(livroRepository.existsByCategoriaId(1L)).thenReturn(false);

        categoriaService.deleteCategoria(1L);

        verify(categoriaRepository, times(1)).delete(categoria);
    }
}
