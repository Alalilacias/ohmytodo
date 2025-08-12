package com.ohmy.todo.utils;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.response.PageResponse;
import org.springframework.data.domain.Page;

public final class PageResponseMapper {

    private PageResponseMapper(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static PageResponse<TodoDto> toPageResponse(Page<TodoDto> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getNumber(),
                page.isFirst(),
                page.isLast(),
                page.getSize(),
                page.getNumberOfElements()
        );
    }
}
