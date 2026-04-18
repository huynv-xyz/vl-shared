package com.vlife.shared.api.controller;

import com.vlife.shared.api.dto.ApiResponse;
import com.vlife.shared.api.dto.PageMeta;
import com.vlife.shared.api.dto.PageRequest;
import com.vlife.shared.api.dto.PageResult;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /* ============ response helpers ============ */

    protected <T> HttpResponse<ApiResponse<T>> ok(T data) {
        return HttpResponse.ok(ApiResponse.success(data));
    }

    protected HttpResponse<ApiResponse<Object>> created(Object data) {
        return HttpResponse.created(ApiResponse.success(data));
    }

    protected HttpResponse<?> noContent() {
        return HttpResponse.noContent();
    }

    /* ============ error helpers (INT CODE) ============ */

    protected <T> HttpResponse<ApiResponse<T>> error400(int code, String message) {
        return error(HttpStatus.BAD_REQUEST, code, message);
    }

    protected <T> HttpResponse<ApiResponse<T>> error403(int code, String message) {
        return error(HttpStatus.FORBIDDEN, code, message);
    }

    protected <T> HttpResponse<ApiResponse<T>> error404(int code, String message) {
        return error(HttpStatus.NOT_FOUND, code, message);
    }

    protected <T> HttpResponse<ApiResponse<T>> error500(int code, String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, code, message);
    }

    protected <T> HttpResponse<ApiResponse<T>> error(
            HttpStatus status,
            int code,
            String message
    ) {
        return HttpResponse
                .status(status)
                .body(ApiResponse.error(code, message));
    }

    /* ============ paging helpers ============ */

    protected PageRequest pageOf(
            @Nullable Integer page,
            @Nullable Integer size,
            @Nullable String sort
    ) {
        return PageRequest.of(page, size, sort);
    }

    protected <T> HttpResponse<ApiResponse<List<T>>> paged(PageResult<T> r) {
        return HttpResponse.ok(
                ApiResponse.success(r.items(), PageMeta.from(r))
        );
    }
}