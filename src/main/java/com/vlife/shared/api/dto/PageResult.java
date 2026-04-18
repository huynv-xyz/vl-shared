package com.vlife.shared.api.dto;

import java.util.List;

public record PageResult<T>(
        List<T> items,
        long total,
        int page,
        int size
) {
    public int totalPages() {
        if (size <= 0) return 0;
        long tp = (total + size - 1) / size;
        return (int) Math.min(tp, Integer.MAX_VALUE);
    }

    public boolean hasNext() {
        return (long) (page + 1) * size < total;
    }

    public static <T> PageResult<T> of(List<T> items, long total, PageRequest req) {
        return new PageResult<>(items, total, req.page(), req.size());
    }
}
