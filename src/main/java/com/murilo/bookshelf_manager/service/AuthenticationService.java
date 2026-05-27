package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.config.security.TokenProvider;
import com.murilo.bookshelf_manager.dto.usuario.LoginRequestDTO;
import com.murilo.bookshelf_manager.dto.usuario.LoginResponseDTO;
import com.murilo.bookshelf_manager.dto.usuario.UsuarioRequestDTO;
import com.murilo.bookshelf_manager.entity.Usuario;
import com.murilo.bookshelf_manager.exception.BusinessException;
import com.murilo.bookshelf_manager.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public void register(UsuarioRequestDTO dto){
        if(usuarioRepository.findByEmail(dto.email()).isPresent()){
            throw new BusinessException("Usuário já cadastrado com este email.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        usuarioRepository.save(usuario);
    }

    public LoginResponseDTO login(LoginRequestDTO dto){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));
            String token = tokenProvider.gerarToken(authentication);

            return new LoginResponseDTO(token);

        } catch (BadCredentialsException e){
            throw new BusinessException("Credenciais inválidas.");
        }
    }
}
