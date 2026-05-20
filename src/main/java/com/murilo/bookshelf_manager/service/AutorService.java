package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.autor.AutorRequestDTO;
import com.murilo.bookshelf_manager.dto.autor.AutorResponseDTO;
import com.murilo.bookshelf_manager.entity.Autor;
import com.murilo.bookshelf_manager.exception.BusinessException;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.AutorRepository;
import com.murilo.bookshelf_manager.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    //post
    @CacheEvict(value = "autores", allEntries = true)
    public AutorResponseDTO createAutor(AutorRequestDTO dto){
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        Autor autorSalvo = autorRepository.save(autor);
        return new AutorResponseDTO(
                autorSalvo.getId(),
                autorSalvo.getNome()
        );
    }


    //get
    @Cacheable("autores")
    public List<AutorResponseDTO> findAllAutores(){
        return autorRepository.findAll()
                .stream()
                .map(autor -> new AutorResponseDTO(autor.getId(),autor.getNome()))
                .toList();
    }


    public List<AutorResponseDTO> findByNome(String nome){
        return autorRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(autor -> new AutorResponseDTO(autor.getId(), autor.getNome()))
                .toList();
    }

    public AutorResponseDTO findById(Long id){
        Autor autorEncontrado = autorRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Autor não encontrado"));

        return new AutorResponseDTO(autorEncontrado.getId(), autorEncontrado.getNome());
    }

    //put
    @CacheEvict(value = "autores", allEntries = true)
    public AutorResponseDTO updateAutor(Long id, AutorRequestDTO dto){
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor não encontrado"));

        autor.setNome(dto.nome());

        Autor autorAtualizado = autorRepository.save(autor);

        return new AutorResponseDTO(autorAtualizado.getId(),
                autorAtualizado.getNome());
    }

    //delete
    @CacheEvict(value = "autores", allEntries = true)
    public void deleteAutor(Long id){
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor não encontrado"));

        if(livroRepository.existsByAutorId(id)) {
            throw new BusinessException("Não é possível excluir um autor que possui livros cadastrados");
        }

        autorRepository.delete(autor);
    }
}
