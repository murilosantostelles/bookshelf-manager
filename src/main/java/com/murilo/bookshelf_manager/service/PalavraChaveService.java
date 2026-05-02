package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.entity.Livro;
import com.murilo.bookshelf_manager.entity.PalavraChave;
import com.murilo.bookshelf_manager.repository.PalavraChaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PalavraChaveService {

    private final PalavraChaveRepository palavraChaveRepository;

    public void savePalavrasChave(List<String> palavras, Livro livro){
        if(palavras!= null){
            for(String palavra: palavras){
                PalavraChave palavraChave = new PalavraChave();
                palavraChave.setPalavra(palavra);
                palavraChave.setLivro(livro);
                palavraChaveRepository.save(palavraChave);
            }
        }
    }

    public void deletePalavrasChave(Long livroId){
        palavraChaveRepository.deleteAll(
                palavraChaveRepository.findByLivroId(livroId)
        );
    }

    public List<String> findPalavrasByLivro(Long livroId){
        return palavraChaveRepository.findByLivroId(livroId)
                .stream()
                .map(PalavraChave::getPalavra)
                .toList();
    }
}
