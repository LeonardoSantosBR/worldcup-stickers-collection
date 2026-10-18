package com.leonardo.worldcup_stickers.dto;

import com.leonardo.worldcup_stickers.entities.StickerEntity;
import com.leonardo.worldcup_stickers.enums.PositionEnum;
import com.leonardo.worldcup_stickers.enums.RarityEnum;
import com.leonardo.worldcup_stickers.enums.StickerGroupEnum;

public record StickerDto(
        Long id,
        Integer number,
        String playerName,
        String country,
        StickerGroupEnum group,
        PositionEnum position,
        RarityEnum rarity,
        String imageUrl) {

    public static StickerDto fromEntity(StickerEntity sticker) {
        return new StickerDto(
                sticker.getId(),
                sticker.getNumber(),
                sticker.getPlayerName(),
                sticker.getCountry(),
                sticker.getGroup(),
                sticker.getPosition(),
                sticker.getRarity(),
                sticker.getImageUrl());
    }
}
