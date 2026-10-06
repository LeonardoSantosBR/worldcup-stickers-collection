package com.leonardo.worldcup_stickers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilter;
import com.leonardo.worldcup_stickers.dto.MyProfileDto;
import com.leonardo.worldcup_stickers.dto.MyStickerDto;
import com.leonardo.worldcup_stickers.dto.PageResponseDto;
import com.leonardo.worldcup_stickers.services.MyProfileService;

@RestController
@RequestMapping("/my-profile")
public class MyProfileController {
    private final MyProfileService myProfileService;

    public MyProfileController(MyProfileService myProfileService) {
        this.myProfileService = myProfileService;
    }

    @GetMapping("")
    public MyProfileDto myStickers(@RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId) {
        return this.myProfileService.findMyUser(userId);
    }

    @GetMapping("/my-stickers")
    public PageResponseDto<MyStickerDto> myStickers(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return this.myProfileService.findMyStickers(userId, page, limit);
    }

    @GetMapping("/my-stickers-available-trade")
    public PageResponseDto<MyStickerDto> myStickersAvailableTrade(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return this.myProfileService.findMyStickersAvailableTrade(userId, page, limit);
    }
}
