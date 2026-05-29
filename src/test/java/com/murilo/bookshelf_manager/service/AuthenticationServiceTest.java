package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.config.security.TokenProvider;
import com.murilo.bookshelf_manager.dto.usuario.LoginRequestDTO;
import com.murilo.bookshelf_manager.dto.usuario.LoginResponseDTO;
import com.murilo.bookshelf_manager.dto.usuario.UsuarioRequestDTO;
import com.murilo.bookshelf_manager.entity.Usuario;
import com.murilo.bookshelf_manager.exception.BusinessException;
import com.murilo.bookshelf_manager.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {


    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;
    private LoginRequestDTO loginRequestDTO;


    @BeforeEach
    void setUp(){
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Murilo");
        usuario.setEmail("murilo@email.com");
        usuario.setSenha("senha123");

        usuarioRequestDTO = new UsuarioRequestDTO("Murilo", "murilo@email.com", "Senha@123");
        loginRequestDTO = new LoginRequestDTO("murilo@email.com", "Senha@123");
    }

    @Test
    void deveRegistrarUsuario(){
        when(usuarioRepository.findByEmail("murilo@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Senha@123")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        authenticationService.register(usuarioRequestDTO);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode("Senha@123");
    }

    @Test
    void deveLancarExceptionQuandoEmailJaCadastrado(){
        when(usuarioRepository.findByEmail("murilo@email.com")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> authenticationService.register(usuarioRequestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Usuário já cadastrado com este email.");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveRealizarLogin(){
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.gerarToken(authentication)).thenReturn("token.jwt.aqui");

        LoginResponseDTO loginResponseDTO = authenticationService.login(loginRequestDTO);

        assertThat(loginResponseDTO).isNotNull();
        assertThat(loginResponseDTO.token()).isEqualTo("token.jwt.aqui");
        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenProvider, times(1)).gerarToken(authentication);
    }

    @Test
    void deveLancarExceptionQuandoCredenciaisInvalidas(){
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad credentials"));

        assertThatThrownBy(() -> authenticationService.login(loginRequestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Credenciais inválidas.");
    }

}
