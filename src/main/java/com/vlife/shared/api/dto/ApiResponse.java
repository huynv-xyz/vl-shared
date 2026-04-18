package com.vlife.shared.api.dto;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ApiResponse<T>(
        int code,
        String msg,
        @Nullable T data,
        @Nullable Object meta
) {

    /* ========== success ========== */

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data, null);
    }

    public static <T> ApiResponse<T> success(T data, Object meta) {
        return new ApiResponse<>(0, "success", data, meta);
    }

    /* ========== error ========== */

    public static <T> ApiResponse<T> error(int code, String msg) {
        return new ApiResponse<>(code, msg, null, null);
    }

    public static <T> ApiResponse<T> error(int code, String msg, Object meta) {
        return new ApiResponse<>(code, msg, null, meta);
    }
}
