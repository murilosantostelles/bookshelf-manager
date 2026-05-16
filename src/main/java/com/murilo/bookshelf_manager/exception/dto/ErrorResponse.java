package com.murilo.bookshelf_manager.exception.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String mensagem,
        Integer status,
        LocalDateTime timestamp
) {
}
