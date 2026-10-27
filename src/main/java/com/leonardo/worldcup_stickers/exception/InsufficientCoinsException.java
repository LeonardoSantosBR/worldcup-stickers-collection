package com.leonardo.worldcup_stickers.exception;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiExceptionConfig;

public class InsufficientCoinsException extends ApiExceptionConfig {
    public InsufficientCoinsException() {
        super(HttpStatus.BAD_REQUEST, "Insufficient coins to open a package");
    }
}
