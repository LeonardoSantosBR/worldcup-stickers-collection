package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.leonardo.worldcup_stickers.dto.UserCreatedDto;
import com.leonardo.worldcup_stickers.dto.UserCreateDto;
import com.leonardo.worldcup_stickers.services.UsersService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<UserCreatedDto> create(@Valid @RequestBody UserCreateDto user) {
        Long userId = this.usersService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserCreatedDto(userId));
    }
}
