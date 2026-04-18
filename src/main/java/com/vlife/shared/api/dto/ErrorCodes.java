package com.vlife.shared.api.dto;

public interface ErrorCodes {
    int OK = 0;

    int INVALID_ARGUMENT = 40001;
    int NOT_FOUND        = 40401;
    int FORBIDDEN        = 40301;
    int CONFLICT         = 40901;

    int INTERNAL_ERROR   = 50000;
}
