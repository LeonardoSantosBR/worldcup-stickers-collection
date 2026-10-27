package com.leonardo.worldcup_stickers.exception;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiExceptionConfig;

public class EmailAlreadyExistsException extends ApiExceptionConfig {
    public EmailAlreadyExistsException(String email) {
        super(HttpStatus.CONFLICT, "User with this email already exists: " + email);
    }
}
