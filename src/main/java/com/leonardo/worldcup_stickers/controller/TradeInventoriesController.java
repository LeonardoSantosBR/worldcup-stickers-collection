package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilter;
import com.leonardo.worldcup_stickers.dto.AvailableTradeStickerDto;
import com.leonardo.worldcup_stickers.dto.MakeAvailableTradeDto;
import com.leonardo.worldcup_stickers.dto.PageResponseDto;
import com.leonardo.worldcup_stickers.dto.TradeInventoryDto;
import com.leonardo.worldcup_stickers.services.UserTradeInventoriesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/trade-inventories")
public class TradeInventoriesController {
    private final UserTradeInventoriesService userTradeInventoriesService;

    public TradeInventoriesController(UserTradeInventoriesService userTradeInventoriesService) {
        this.userTradeInventoriesService = userTradeInventoriesService;
    }

    @PostMapping("/make-available-trade")
    public TradeInventoryDto makeAvailableTrade(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @Valid @RequestBody MakeAvailableTradeDto body) {
        return userTradeInventoriesService.makeAvailableTrade(userId, body.stickerIds());
    }

    @GetMapping("/available-trades")
    public PageResponseDto<AvailableTradeStickerDto> availableTrades(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String name,
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId) {
        return userTradeInventoriesService.findAllAvailableForTrade(userId, page, limit, name);
    }

    @PatchMapping("/clear-available-trades")
    public TradeInventoryDto clearAvailableTrades(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId) {
        return userTradeInventoriesService.clearAvailableTrades(userId);
    }
}
