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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final PalavraChaveService palavraChaveService;

    //post
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

        palavraChaveService.savePalavrasChave(dto.palavrasChave(),livroSalvo);


        return toResponseDTO(livroSalvo);
    }

    //get
    public Page<LivroResponseDTO> findAllLivros(Pageable pageable) {
        return livroRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByTitulo( String titulo, Pageable pageable){
        return livroRepository.findByTituloContainingIgnoreCase(titulo,pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByAutor(String nome, Pageable pageable){
        return livroRepository.findByAutorNomeContainingIgnoreCase(nome,pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByCategoria(String nome, Pageable pageable){
        return livroRepository.findByCategoriaNomeContainingIgnoreCase(nome, pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByPalavraChave(String palavra, Pageable pageable){
        return livroRepository.findByPalavrasChaveContainingIgnoreCase(palavra,pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByEditora(String editora, Pageable pageable){
        return livroRepository.findByEditoraContainingIgnoreCase(editora,pageable)
                .map(this::toResponseDTO);
    }

    private LivroResponseDTO toResponseDTO(Livro livro) {
        List<String> palavrasChave = palavraChaveService.findPalavrasByLivro(livro.getId());
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getEditora(),
                livro.getDescricao(),
                livro.getCapaUrl(),
                livro.getStatus(),
                livro.getAutor().getNome(),
                livro.getCategoria().getNome(),
                palavrasChave
        );
    }

    //put patch
    public LivroResponseDTO updateLivro(Long id, LivroRequestDTO dto){
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(()-> new RuntimeException("Autor não encontrado"));
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(()-> new RuntimeException("Categoria não encontrada"));

        livro.setTitulo(dto.titulo());
        livro.setEditora(dto.editora());
        livro.setDescricao(dto.descricao());
        livro.setCapaUrl(dto.capaUrl());
        livro.setStatus(dto.status());
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        palavraChaveService.deletePalavrasChave(id);
        palavraChaveService.savePalavrasChave(dto.palavrasChave(), livro);

        Livro livroAtualizado = livroRepository.save(livro);
        return toResponseDTO(livroAtualizado);
    }

    //delete
    public void deleteLivro(Long id){
        Livro livro = livroRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        palavraChaveService.deletePalavrasChave(id);
        livroRepository.delete(livro);
    }
}
