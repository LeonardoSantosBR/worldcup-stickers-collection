package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.dto.AuthDto;
import com.leonardo.worldcup_stickers.dto.TokenResponseDto;
import com.leonardo.worldcup_stickers.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Authentication", description = "Sign in and obtain an access token")
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Sign in", description = "Authenticates a user with email and password and returns a JWT access token.")
    @PostMapping("/signin")
    public TokenResponseDto signin(@Valid @RequestBody AuthDto body) {
        String token = authService.signin(body.email(), body.password());
        return new TokenResponseDto(token);
    }
}
