package com.leonardo.worldcup_stickers.exceptions;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiException;

public class InsufficientBalanceException extends ApiException {
    public InsufficientBalanceException() {
        super(HttpStatus.BAD_REQUEST, "Insufficient money balance to convert to coins");
    }
}
