package com.leonardo.worldcup_stickers.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilterConfig;
import com.leonardo.worldcup_stickers.dto.StickerDto;
import com.leonardo.worldcup_stickers.service.StickersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Stickers", description = "Open sticker packs and manage sticker data")
@RequestMapping("/stickers")
public class StickersController {
    private final StickersService stickersService;

    public StickersController(StickersService stickersService) {
        this.stickersService = stickersService;
    }

    @PostMapping("/open-package")
    @Operation(summary = "Open a sticker pack", description = "Draws seven random stickers and adds them to the authenticated user's collection.")
    public List<StickerDto> openPackage(@RequestAttribute(JwtAuthFilterConfig.USER_ID_ATTRIBUTE) Long userId) {
        return stickersService.openPackage(userId).stream().map(StickerDto::fromEntity).toList();
    }
}
