package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilter;
import com.leonardo.worldcup_stickers.dto.AddMoneyDto;
import com.leonardo.worldcup_stickers.service.UserFinancialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users-coins")
public class UserCoinsController {
    private final UserFinancialService userFinancialService;

    public UserCoinsController(UserFinancialService userFinancialService) {
        this.userFinancialService = userFinancialService;
    }

    @PostMapping()
    public boolean addCoins(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @Valid @RequestBody AddMoneyDto body) {
        return userFinancialService.addCoins(userId, body.amount());
    }
}
