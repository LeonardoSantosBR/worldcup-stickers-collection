package com.leonardo.worldcup_stickers.config;

import org.springframework.http.HttpStatus;

public class ApiExceptionConfig extends RuntimeException {
    private final HttpStatus status;

    protected ApiExceptionConfig(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
