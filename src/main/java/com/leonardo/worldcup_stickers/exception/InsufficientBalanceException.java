package com.leonardo.worldcup_stickers.exception;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiExceptionConfig;

public class InsufficientBalanceException extends ApiExceptionConfig {
    public InsufficientBalanceException() {
        super(HttpStatus.BAD_REQUEST, "Insufficient money balance to convert to coins");
    }
}
