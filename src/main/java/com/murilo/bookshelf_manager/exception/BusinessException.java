package com.murilo.bookshelf_manager.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String mensagem){
        super(mensagem);
    }
}
