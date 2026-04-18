package com.vlife.shared.api.handler;

import com.vlife.shared.api.dto.ApiResponse;
import com.vlife.shared.exception.BadRequestException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;

@Produces
@Singleton
public class BadRequestExceptionHandler implements ExceptionHandler<BadRequestException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(
            HttpRequest request,
            BadRequestException ex
    ) {
        return HttpResponse.badRequest(
                ApiResponse.error(-400, ex.getMessage())
        );
    }

}
