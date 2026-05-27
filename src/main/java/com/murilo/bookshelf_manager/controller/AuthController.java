package com.murilo.bookshelf_manager.controller;

import com.murilo.bookshelf_manager.dto.usuario.LoginRequestDTO;
import com.murilo.bookshelf_manager.dto.usuario.LoginResponseDTO;
import com.murilo.bookshelf_manager.dto.usuario.UsuarioRequestDTO;
import com.murilo.bookshelf_manager.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO){
        authenticationService.register(usuarioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO));
    }
}
