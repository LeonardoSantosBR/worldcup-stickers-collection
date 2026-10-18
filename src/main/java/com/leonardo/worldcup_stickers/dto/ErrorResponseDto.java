package com.leonardo.worldcup_stickers.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors) {

    public static ErrorResponseDto of(int status, String error, String message) {
        return new ErrorResponseDto(Instant.now(), status, error, message, Map.of());
    }
}
