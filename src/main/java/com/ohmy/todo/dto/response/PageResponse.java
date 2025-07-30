package com.ohmy.todo.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int totalPages,
        int number,
        boolean first,
        boolean last,
        int size,
        int numberOfElements
) {}