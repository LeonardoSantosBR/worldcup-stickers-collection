package com.leonardo.worldcup_stickers.exception;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiExceptionConfig;

public class StickersNotOwnedException extends ApiExceptionConfig {
    public StickersNotOwnedException(Collection<Long> stickerIds) {
        super(HttpStatus.BAD_REQUEST, "Stickers not owned by user: " + stickerIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
    }
}
