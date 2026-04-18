package com.vlife.shared.api.handler;

import com.vlife.shared.api.dto.ApiResponse;
import com.vlife.shared.exception.ForbiddenException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
class ForbiddenExceptionHandler implements ExceptionHandler<ForbiddenException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, ForbiddenException e) {
        return HttpResponse.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(-403, e.getMessage()));
    }
}
