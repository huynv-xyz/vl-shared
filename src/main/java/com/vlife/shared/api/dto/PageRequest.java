package com.vlife.shared.api.dto;

public record PageRequest(int page, int size, String sort) {
    public int offset() { return Math.max(page, 0) * Math.max(size, 0); }

    public static PageRequest of(Integer page, Integer size, String sort) {
        return new PageRequest(page == null ? 0 : page, size == null ? 20 : size, sort);
    }
}