package com.leonardo.worldcup_stickers.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.worldcup_stickers.dto.MyProfileDto;
import com.leonardo.worldcup_stickers.dto.MyStickerDto;
import com.leonardo.worldcup_stickers.dto.PageResponseDto;
import com.leonardo.worldcup_stickers.entities.UserEntity;
import com.leonardo.worldcup_stickers.entities.UserStickerEntity;
import com.leonardo.worldcup_stickers.entities.UserTradeInventoryEntity;
import com.leonardo.worldcup_stickers.exceptions.UserNotFoundException;
import com.leonardo.worldcup_stickers.repositories.MyProfileRepository;

@Service
public class MyProfileService {
    private static final int MAX_LIMIT = 100;
    private final MyProfileRepository myProfileRepository;

    public MyProfileService(MyProfileRepository myProfileRepository) {
        this.myProfileRepository = myProfileRepository;
    }

    @Transactional(readOnly = true)
    public MyProfileDto findMyUser(Long userId) {
        UserEntity user = myProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        long ownedStickers = myProfileRepository.countOwnedStickersByUserId(userId);
        long totalStickers = myProfileRepository.countTotalStickers();
        return MyProfileDto.fromEntity(user, ownedStickers, totalStickers);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<MyStickerDto> findMyStickers(Long userId, int page, int limit) {
        Pageable pageable = createPageable(page, limit);
        Page<UserStickerEntity> result = myProfileRepository.findStickersByUserId(userId, pageable);
        return PageResponseDto.from(result, MyStickerDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<MyStickerDto> findMyStickersAvailableTrade(Long userId, int page, int limit) {
        Pageable pageable = createPageable(page, limit);
        List<Long> availableStickerIds = myProfileRepository.findTradeInventoryByUserId(userId)
                .map(UserTradeInventoryEntity::getAvailableStickerIds)
                .orElseGet(List::of);
        if (availableStickerIds.isEmpty()) {
            return PageResponseDto.from(Page.<UserStickerEntity>empty(pageable), MyStickerDto::fromEntity);
        }
        Page<UserStickerEntity> result = myProfileRepository.findStickersByUserIdAndStickerIdIn(
                userId, availableStickerIds, pageable);
        return PageResponseDto.from(result, MyStickerDto::fromEntity);
    }

    private Pageable createPageable(int page, int limit) {
        return PageRequest.of(
                Math.max(page - 1, 0),
                Math.min(Math.max(limit, 1), MAX_LIMIT),
                Sort.by("sticker.number").ascending());
    }
}
