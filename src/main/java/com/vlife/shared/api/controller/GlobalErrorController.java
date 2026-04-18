package com.vlife.shared.api.controller;

import com.vlife.shared.api.dto.ApiResponse;
import com.vlife.shared.exception.BusinessException;
import com.vlife.shared.exception.ForbiddenException;
import com.vlife.shared.exception.UnauthorizedException;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GlobalErrorController {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorController.class);

    // ========================
    // 404
    // ========================
    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public HttpResponse<ApiResponse<?>> handleNotFound(HttpRequest<?> request) {
        return HttpResponse
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(-404, "not_found"));
    }

    // ========================
    // 400 (generic)
    // ========================
    @Error(status = HttpStatus.BAD_REQUEST, global = true)
    public HttpResponse<ApiResponse<?>> handleBadRequest(HttpRequest<?> request, Throwable e) {

        log.warn("Bad request on {} {}: {}", request.getMethod(), request.getUri(), e.getMessage());

        if (e instanceof BusinessException be) {
            return HttpResponse
                    .badRequest()
                    .body(ApiResponse.error(
                            be.getCode(),
                            be.getMessage(),
                            be.getData()
                    ));
        }

        return HttpResponse
                .badRequest()
                .body(ApiResponse.error(-400, e.getMessage())); // 👈 trả message thật
    }

    // ========================
    // GLOBAL
    // ========================
    @Error(global = true)
    public HttpResponse<ApiResponse<?>> handleThrowable(HttpRequest<?> request, Throwable e) {

        log.error("Unhandled error on {} {}: {}", request.getMethod(), request.getUri(), e.getMessage(), e);

        // 🔥 BUSINESS EXCEPTION (QUAN TRỌNG)
        if (e instanceof BusinessException be) {
            return HttpResponse
                    .badRequest()
                    .body(ApiResponse.error(
                            be.getCode(),
                            be.getMessage(),
                            be.getData()
                    ));
        }

        // 401
        if (e instanceof UnauthorizedException) {
            return HttpResponse
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(-401, e.getMessage()));
        }

        // 403
        if (e instanceof ForbiddenException) {
            return HttpResponse
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(-403, e.getMessage()));
        }

        // 500
        return HttpResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(-500, "internal_server_error"));
    }
}