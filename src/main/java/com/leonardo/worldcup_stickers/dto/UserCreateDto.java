package com.leonardo.worldcup_stickers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password) {
}
