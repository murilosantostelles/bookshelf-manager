package com.murilo.bookshelf_manager.service;

import com.murilo.bookshelf_manager.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }
}
