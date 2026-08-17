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

    @Mock
    private UsuarioRepository usuarioRepository;

    private Emprestimo emprestimo;
    private EmprestimoRequestDTO emprestimoRequestDTO;
    private Livro livro;
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@email.com");
        usuario.setNome("Test");
        usuario.setSenha("senha123");

        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Dom Casmurro");
        livro.setStatus(Status.DISPONIVEL);
        livro.setUsuario(usuario);

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

    private void mockUsuarioLogado(MockedStatic<SecurityContextHolder> securityContextHolder) {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
    }

    @Test
    void deveCriarEmprestimo(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockUsuarioLogado(securityContextHolder);
            when(livroRepository.findByIdAndUsuario(1L, usuario)).thenReturn(Optional.of(livro));
            when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);

            EmprestimoResponseDTO response = emprestimoService.createEmprestimo(emprestimoRequestDTO);

            assertThat(response).isNotNull();
            assertThat(response.nomePessoa()).isEqualTo("João Silva");
            assertThat(response.livroTitulo()).isEqualTo("Dom Casmurro");
            verify(livroRepository, times(1)).save(any(Livro.class));
            verify(emprestimoRepository, times(1)).save(any(Emprestimo.class));
        }
    }

    @Test
    void deveImpedirEmprestimoDeLivroIndisponivel(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockUsuarioLogado(securityContextHolder);
            livro.setStatus(Status.EMPRESTADO);
            when(livroRepository.findByIdAndUsuario(1L, usuario)).thenReturn(Optional.of(livro));

            assertThatThrownBy(() -> emprestimoService.createEmprestimo(emprestimoRequestDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("O Livro Dom Casmurro já está emprestado.");

            verify(emprestimoRepository, never()).save(any(Emprestimo.class));
        }
    }

    @Test
    void deveLancarExceptionQuandoLivroNaoEncontrado(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockUsuarioLogado(securityContextHolder);

            EmprestimoRequestDTO dtoComLivroInexistente = new EmprestimoRequestDTO(
                    "João Silva",
                    LocalDate.now(),
                    null,
                    2L
            );

            when(livroRepository.findByIdAndUsuario(2L, usuario)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emprestimoService.createEmprestimo(dtoComLivroInexistente))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Livro não encontrado");

            verify(emprestimoRepository, never()).save(any(Emprestimo.class));
        }
    }

    @Test
    void deveDeletarEmprestimo(){
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockUsuarioLogado(securityContextHolder);
            when(emprestimoRepository.findByIdAndLivroUsuario(1L, usuario)).thenReturn(Optional.of(emprestimo));

            emprestimoService.deleteEmprestimo(1L);

            assertThat(livro.getStatus()).isEqualTo(Status.DISPONIVEL);
            verify(livroRepository, times(1)).save(any(Livro.class));
            verify(emprestimoRepository, times(1)).delete(emprestimo);
        }
    }
}