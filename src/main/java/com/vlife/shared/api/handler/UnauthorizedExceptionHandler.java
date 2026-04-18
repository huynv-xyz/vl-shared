package com.vlife.shared.api.handler;

import com.vlife.shared.api.dto.ApiResponse;
import com.vlife.shared.exception.UnauthorizedException;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
public class UnauthorizedExceptionHandler
        implements ExceptionHandler<UnauthorizedException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, UnauthorizedException e) {
        return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(-401, e.getMessage()));
    }
}