package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilterConfig;
import com.leonardo.worldcup_stickers.dto.MyProfileDto;
import com.leonardo.worldcup_stickers.dto.MyStickerDto;
import com.leonardo.worldcup_stickers.dto.PageResponseDto;
import com.leonardo.worldcup_stickers.service.MyProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@Tag(name = "My profile", description = "View the authenticated user's profile and sticker collection")
@RequestMapping("/my-profile")
public class MyProfileController {
    private final MyProfileService myProfileService;

    public MyProfileController(MyProfileService myProfileService) {
        this.myProfileService = myProfileService;
    }

    @GetMapping("")
    @Operation(summary = "Get my profile", description = "Returns the authenticated user's profile and album completion percentage.")
    public MyProfileDto myStickers(@RequestAttribute(JwtAuthFilterConfig.USER_ID_ATTRIBUTE) Long userId) {
        return this.myProfileService.findMyUser(userId);
    }

    @GetMapping("/my-stickers")
    @Operation(summary = "List my stickers", description = "Returns a paginated list of stickers owned by the authenticated user.")
    public PageResponseDto<MyStickerDto> myStickers(
            @RequestAttribute(JwtAuthFilterConfig.USER_ID_ATTRIBUTE) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return this.myProfileService.findMyStickers(userId, page, limit);
    }

    @GetMapping("/my-stickers-available-trade")
    @Operation(summary = "List my tradeable stickers", description = "Returns the authenticated user's stickers currently listed as available for trade.")
    public PageResponseDto<MyStickerDto> myStickersAvailableTrade(
            @RequestAttribute(JwtAuthFilterConfig.USER_ID_ATTRIBUTE) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return this.myProfileService.findMyStickersAvailableTrade(userId, page, limit);
    }
}
