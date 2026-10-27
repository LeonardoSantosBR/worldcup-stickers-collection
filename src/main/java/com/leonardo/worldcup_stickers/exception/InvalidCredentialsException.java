package com.leonardo.worldcup_stickers.exception;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiExceptionConfig;

public class InvalidCredentialsException extends ApiExceptionConfig {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}
