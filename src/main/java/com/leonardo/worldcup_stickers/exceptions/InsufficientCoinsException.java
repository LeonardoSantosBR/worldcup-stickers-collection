package com.leonardo.worldcup_stickers.exceptions;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiException;

public class InsufficientCoinsException extends ApiException {
    public InsufficientCoinsException() {
        super(HttpStatus.BAD_REQUEST, "Insufficient coins to open a package");
    }
}
