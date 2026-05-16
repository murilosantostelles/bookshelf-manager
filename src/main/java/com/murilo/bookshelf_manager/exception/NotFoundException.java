package com.murilo.bookshelf_manager.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String mensagem){
        super(mensagem);
    }
}
