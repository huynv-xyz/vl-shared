package com.vlife.shared.api.dto;

public record PageMeta(int page, int size, long total, int totalPages, boolean hasNext) {
    public static PageMeta from(PageResult<?> r) {
        return new PageMeta(r.page(), r.size(), r.total(), r.totalPages(), r.hasNext());
    }
}
