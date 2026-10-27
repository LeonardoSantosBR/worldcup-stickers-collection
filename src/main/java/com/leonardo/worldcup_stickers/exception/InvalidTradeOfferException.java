package com.leonardo.worldcup_stickers.exception;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiExceptionConfig;

public class InvalidTradeOfferException extends ApiExceptionConfig {
    public InvalidTradeOfferException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
