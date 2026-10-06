package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilter;
import com.leonardo.worldcup_stickers.dto.AddBalanceDto;
import com.leonardo.worldcup_stickers.services.UserFinancialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserFinancialController {
    private final UserFinancialService userFinancialService;

    public UserFinancialController(UserFinancialService userFinancialService) {
        this.userFinancialService = userFinancialService;
    }

    @PostMapping("/add-balance")
    public boolean addBalance(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @Valid @RequestBody AddBalanceDto body) {
        return userFinancialService.addBalance(userId, body.amount());
    }

    @PostMapping("/add-coins")
    public boolean addCoins(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @Valid @RequestBody AddBalanceDto body) {
        return userFinancialService.addCoins(userId, body.amount());
    }
}
