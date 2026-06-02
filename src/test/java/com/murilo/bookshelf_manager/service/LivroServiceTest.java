package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.livro.LivroRequestDTO;
import com.murilo.bookshelf_manager.dto.livro.LivroResponseDTO;
import com.murilo.bookshelf_manager.entity.Autor;
import com.murilo.bookshelf_manager.entity.Categoria;
import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.entity.Usuario;
import com.murilo.bookshelf_manager.enums.Status;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.AutorRepository;
import com.murilo.bookshelf_manager.repository.CategoriaRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import com.murilo.bookshelf_manager.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private PalavraChaveService palavraChaveService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Livro livro;
    private Autor autor;
    private Categoria categoria;
    private Usuario usuario;
    private LivroRequestDTO livroRequestDTO;

    @BeforeEach
    void setUp(){
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("Machado de Assis");

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Literatura");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@email.com");
        usuario.setNome("Test");
        usuario.setSenha("senha123");

        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Dom Casmurro");
        livro.setEditora("Editora Teste");
        livro.setStatus(Status.DISPONIVEL);
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setUsuario(usuario);

        livroRequestDTO = new LivroRequestDTO(
                "Dom Casmurro",
                "Editora Teste",
                "Descrição",
                "http://capa.url",
                Status.DISPONIVEL,
                1L,
                1L,
                List.of("clássico", "romance")
        );
    }

    @Test
    void deveCriarLivro(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("test@email.com");
            when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
            when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(livroRepository.save(any(Livro.class))).thenReturn(livro);
            when(palavraChaveService.findPalavrasByLivro(1L)).thenReturn(List.of("clássico", "romance"));

            LivroResponseDTO response = livroService.createLivro(livroRequestDTO);

            assertThat(response).isNotNull();
            assertThat(response.titulo()).isEqualTo("Dom Casmurro");
            assertThat(response.autorNome()).isEqualTo("Machado de Assis");
            verify(livroRepository, times(1)).save(any(Livro.class));
        }
    }

    @Test
    void deveBuscarLivroPorId(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("test@email.com");
            when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
            when(livroRepository.findByIdAndUsuario(1L, usuario)).thenReturn(Optional.of(livro));
            when(palavraChaveService.findPalavrasByLivro(1L)).thenReturn(List.of());

            LivroResponseDTO response = livroService.findLivroById(1L);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.titulo()).isEqualTo("Dom Casmurro");
            verify(livroRepository, times(1)).findByIdAndUsuario(1L, usuario);
        }
    }

    @Test
    void deveLancarExceptionQuandoLivroNaoEncontrado(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("test@email.com");
            when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
            when(livroRepository.findByIdAndUsuario(99L, usuario)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> livroService.findLivroById(99L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Livro não encontrado");
        }
    }

    @Test
    void deveLancarExceptionQuandoAutorNaoExiste(){
        when(autorRepository.findById(99L)).thenReturn(Optional.empty());

        LivroRequestDTO dto = new LivroRequestDTO(
                "Dom Casmurro", "Editora", "Desc",
                "url", Status.DISPONIVEL, 99L, 1L, List.of()
        );

        assertThatThrownBy(() -> livroService.createLivro(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Autor não encontrado");
    }

    @Test
    void deveLancarExceptionQuandoCategoriaNaoExiste(){
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        LivroRequestDTO dto = new LivroRequestDTO(
                "Dom Casmurro", "Editora", "Desc",
                "url", Status.DISPONIVEL, 1L, 99L, List.of()
        );

        assertThatThrownBy(() -> livroService.createLivro(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Categoria não encontrada");
    }

    @Test
    void deveAtualizarLivro(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("test@email.com");
            when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
            when(livroRepository.findByIdAndUsuario(1L, usuario)).thenReturn(Optional.of(livro));
            when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(livroRepository.save(any(Livro.class))).thenReturn(livro);
            when(palavraChaveService.findPalavrasByLivro(1L)).thenReturn(List.of());

            LivroResponseDTO response = livroService.updateLivro(1L, livroRequestDTO);

            assertThat(response).isNotNull();
            assertThat(response.titulo()).isEqualTo("Dom Casmurro");
            verify(livroRepository, times(1)).save(any(Livro.class));
        }
    }

    @Test
    void deveDeletarLivro(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("test@email.com");
            when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
            when(livroRepository.findByIdAndUsuario(1L, usuario)).thenReturn(Optional.of(livro));

            livroService.deleteLivro(1L);

            verify(livroRepository, times(1)).delete(livro);
        }
    }
}