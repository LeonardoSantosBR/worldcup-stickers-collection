package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.leonardo.worldcup_stickers.dto.UserCreatedDto;
import com.leonardo.worldcup_stickers.dto.UserCreateDto;
import com.leonardo.worldcup_stickers.service.UsersService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Users", description = "User registration")
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    @Operation(summary = "Register a user", description = "Creates a user account and returns its identifier.")
    public ResponseEntity<UserCreatedDto> create(@Valid @RequestBody UserCreateDto user) {
        Long userId = this.usersService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserCreatedDto(userId));
    }
}
