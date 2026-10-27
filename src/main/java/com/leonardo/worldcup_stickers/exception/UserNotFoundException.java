package com.leonardo.worldcup_stickers.exception;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiExceptionConfig;

public class UserNotFoundException extends ApiExceptionConfig {
    public UserNotFoundException(Long userId) {
        super(HttpStatus.NOT_FOUND, "User not found: " + userId);
    }
}
