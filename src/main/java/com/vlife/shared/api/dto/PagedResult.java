package com.vlife.shared.api.dto;

import java.util.List;

public record PagedResult<T>(
        List<T> items,
        String nextCursor,
        boolean hasNext
) {
    public static <T> PagedResult<T> of(List<T> items, String nextCursor, boolean hasNext) {
        return new PagedResult<>(items, nextCursor, hasNext);
    }

    public static <T> PagedResult<T> empty() {
        return new PagedResult<>(List.of(), null, false);
    }
}
