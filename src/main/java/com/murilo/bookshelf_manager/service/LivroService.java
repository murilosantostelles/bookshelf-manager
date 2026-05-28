package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.livro.LivroRequestDTO;
import com.murilo.bookshelf_manager.dto.livro.LivroResponseDTO;
import com.murilo.bookshelf_manager.entity.Autor;
import com.murilo.bookshelf_manager.entity.Categoria;
import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.entity.Usuario;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.AutorRepository;
import com.murilo.bookshelf_manager.repository.CategoriaRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import com.murilo.bookshelf_manager.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final PalavraChaveService palavraChaveService;
    private final UsuarioRepository usuarioRepository;

    @CacheEvict(value = "livros", allEntries = true)
    public LivroResponseDTO createLivro(LivroRequestDTO dto){
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new NotFoundException("Autor não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setCapaUrl(dto.capaUrl());
        livro.setDescricao(dto.descricao());
        livro.setStatus(dto.status());
        livro.setEditora(dto.editora());
        livro.setUsuario(getUsuarioLogado());

        Livro livroSalvo = livroRepository.save(livro);
        palavraChaveService.savePalavrasChave(dto.palavrasChave(), livroSalvo);

        return toResponseDTO(livroSalvo);
    }

    @Cacheable("livros")
    public Page<LivroResponseDTO> findAllLivros(Pageable pageable) {
        return livroRepository.findByUsuario(getUsuarioLogado(), pageable)
                .map(this::toResponseDTO);
    }

    @Cacheable("livros")
    public LivroResponseDTO findLivroById(Long id){
        Livro livro = livroRepository.findByIdAndUsuario(id, getUsuarioLogado())
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));
        return toResponseDTO(livro);
    }

    public Page<LivroResponseDTO> findByTitulo(String titulo, Pageable pageable){
        return livroRepository.findByTituloContainingIgnoreCaseAndUsuario(titulo, getUsuarioLogado(), pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByAutor(String nome, Pageable pageable){
        return livroRepository.findByAutorNomeContainingIgnoreCaseAndUsuario(nome, getUsuarioLogado(), pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByCategoria(String nome, Pageable pageable){
        return livroRepository.findByCategoriaNomeContainingIgnoreCaseAndUsuario(nome, getUsuarioLogado(), pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByPalavraChave(String palavra, Pageable pageable){
        return livroRepository.findByPalavrasChavePalavraContainingIgnoreCaseAndUsuario(palavra, getUsuarioLogado(), pageable)
                .map(this::toResponseDTO);
    }

    public Page<LivroResponseDTO> findByEditora(String editora, Pageable pageable){
        return livroRepository.findByEditoraContainingIgnoreCaseAndUsuario(editora, getUsuarioLogado(), pageable)
                .map(this::toResponseDTO);
    }

    @CacheEvict(value = "livros", allEntries = true)
    public LivroResponseDTO updateLivro(Long id, LivroRequestDTO dto){
        Livro livro = livroRepository.findByIdAndUsuario(id, getUsuarioLogado())
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));

        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new NotFoundException("Autor não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

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

    @CacheEvict(value = "livros", allEntries = true)
    public void deleteLivro(Long id){
        Livro livro = livroRepository.findByIdAndUsuario(id, getUsuarioLogado())
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));
        palavraChaveService.deletePalavrasChave(id);
        livroRepository.delete(livro);
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

    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }
}
