package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilterConfig;
import com.leonardo.worldcup_stickers.dto.MakeAvailableTradeDto;
import com.leonardo.worldcup_stickers.dto.PageResponseDto;
import com.leonardo.worldcup_stickers.dto.TradeInventoryDto;
import com.leonardo.worldcup_stickers.dto.availableTrade.AvailableTradeStickerDto;
import com.leonardo.worldcup_stickers.service.UserTradeInventoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Trade inventories", description = "Publish and browse stickers available for trade")
@RequestMapping("/trade-inventories")
public class TradeInventoriesController {
    private final UserTradeInventoriesService userTradeInventoriesService;

    public TradeInventoriesController(UserTradeInventoriesService userTradeInventoriesService) {
        this.userTradeInventoriesService = userTradeInventoriesService;
    }

    @PostMapping("/make-available-trade")
    @Operation(summary = "Publish stickers for trade", description = "Adds the specified owned stickers to the authenticated user's trade inventory.")
    public TradeInventoryDto makeAvailableTrade(
            @RequestAttribute(JwtAuthFilterConfig.USER_ID_ATTRIBUTE) Long userId,
            @Valid @RequestBody MakeAvailableTradeDto body) {
        return userTradeInventoriesService.makeAvailableTrade(userId, body.stickerIds());
    }

    @GetMapping("/available-trades")
    @Operation(summary = "Browse available trades", description = "Returns a paginated listing of stickers offered by other users, optionally filtered by sticker name.")
    public PageResponseDto<AvailableTradeStickerDto> availableTrades(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String name,
            @RequestAttribute(JwtAuthFilterConfig.USER_ID_ATTRIBUTE) Long userId) {
        return userTradeInventoriesService.findAllAvailableForTrade(userId, page, limit, name);
    }

    @PatchMapping("/clear-available-trades")
    @Operation(summary = "Clear my trade inventory", description = "Removes all stickers from the authenticated user's available-for-trade listing.")
    public TradeInventoryDto clearAvailableTrades(
            @RequestAttribute(JwtAuthFilterConfig.USER_ID_ATTRIBUTE) Long userId) {
        return userTradeInventoriesService.clearAvailableTrades(userId);
    }
}
