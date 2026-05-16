package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.dto.categoria.CategoriaRequestDTO;
import com.murilo.bookshelf_manager.dto.categoria.CategoriaResponseDTO;
import com.murilo.bookshelf_manager.entity.Categoria;
import com.murilo.bookshelf_manager.exception.NotFoundException;
import com.murilo.bookshelf_manager.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;


    //post
    public CategoriaResponseDTO createCategoria(CategoriaRequestDTO dto){
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setSubcategoria(dto.subcategoria());
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoriaSalva.getId(),
                categoriaSalva.getNome(),
                categoriaSalva.getSubcategoria());
    }

    //get
    public List<CategoriaResponseDTO> findAllCategorias(){
        return categoriaRepository.findAll()
                .stream()
                .map(categoria -> new CategoriaResponseDTO(categoria.getId(),categoria.getNome(),categoria.getSubcategoria()))
                .toList();
    }

    public List<CategoriaResponseDTO> findByNome(String nome){
        return categoriaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(categoria -> new CategoriaResponseDTO(categoria.getId(),categoria.getNome(),categoria.getSubcategoria()))
                .toList();
    }

    public List<CategoriaResponseDTO> findBySubcategoria(String subcategoria){
        return categoriaRepository.findBySubcategoriaContainingIgnoreCase(subcategoria)
                .stream()
                .map(categoria -> new CategoriaResponseDTO(categoria.getId(),categoria.getNome(),categoria.getSubcategoria()))
                .toList();
    }

    public CategoriaResponseDTO findById (Long id){
        Categoria categoriaEncontrada = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        return new CategoriaResponseDTO(categoriaEncontrada.getId(),categoriaEncontrada.getNome(),categoriaEncontrada.getSubcategoria());
    }

    //put
    public CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO dto){
        Categoria categoriaEncontrada = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        categoriaEncontrada.setNome(dto.nome());
        categoriaEncontrada.setSubcategoria(dto.subcategoria());

        Categoria categoriaAtualizada = categoriaRepository.save(categoriaEncontrada);

        return new CategoriaResponseDTO(categoriaAtualizada.getId(),categoriaAtualizada.getNome(),categoriaAtualizada.getSubcategoria());
    }

    //delete
    public void deleteCategoria(Long id){
        Categoria categoriaEncontrada = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        categoriaRepository.delete(categoriaEncontrada);
    }
}
