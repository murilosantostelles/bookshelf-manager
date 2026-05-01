package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.livro.LivroRequestDTO;
import com.murilo.bookshelf_manager.dto.livro.LivroResponseDTO;
import com.murilo.bookshelf_manager.entity.Autor;
import com.murilo.bookshelf_manager.entity.Categoria;
import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.repository.AutorRepository;
import com.murilo.bookshelf_manager.repository.CategoriaRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LivroResponseDTO createLivro(LivroRequestDTO dto){
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(()-> new RuntimeException("Categoria não encontrada"));

        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setCapaUrl(dto.capaUrl());
        livro.setDescricao(dto.descricao());
        livro.setStatus(dto.status());
        livro.setEditora(dto.editora());

        Livro livroSalvo = livroRepository.save(livro);

        return new LivroResponseDTO(
                livroSalvo.getId(),
                livroSalvo.getTitulo(),
                livroSalvo.getEditora(),
                livroSalvo.getDescricao(),
                livroSalvo.getCapaUrl(),
                livroSalvo.getStatus(),
                livroSalvo.getAutor().getNome(),
                livroSalvo.getCategoria().getNome()
        );
    }
}
