package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.leonardo.worldcup_stickers.dto.UserCreatedDto;
import com.leonardo.worldcup_stickers.dto.UserCreateDto;
import com.leonardo.worldcup_stickers.service.UsersService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Validator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

@RestController
@Tag(name = "Users", description = "User registration")
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public UsersController(UsersService usersService, ObjectMapper objectMapper, Validator validator) {
        this.usersService = usersService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostMapping
    @Operation(summary = "Register a user", description = "Creates a user account and returns its identifier.")
    public ResponseEntity<UserCreatedDto> create(
            @RequestParam("user") String userJson,
            @RequestParam(required = false) MultipartFile avatar) throws JacksonException {
        UserCreateDto user = objectMapper.readValue(userJson, UserCreateDto.class);
        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);
        if (!violations.isEmpty())
            throw new jakarta.validation.ConstraintViolationException(violations);
        Long userId = this.usersService.create(user, avatar);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserCreatedDto(userId));
    }
}
