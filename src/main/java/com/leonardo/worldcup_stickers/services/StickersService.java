package com.leonardo.worldcup_stickers.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.worldcup_stickers.entities.StickerEntity;
import com.leonardo.worldcup_stickers.entities.UserFinancialEntity;
import com.leonardo.worldcup_stickers.entities.UserEntity;
import com.leonardo.worldcup_stickers.entities.UserStickerEntity;
import com.leonardo.worldcup_stickers.enums.RarityEnum;
import com.leonardo.worldcup_stickers.exceptions.UserNotFoundException;
import com.leonardo.worldcup_stickers.exceptions.InsufficientCoinsException;
import com.leonardo.worldcup_stickers.repositories.StickersRepository;
import com.leonardo.worldcup_stickers.repositories.UserFinancialRepository;
import com.leonardo.worldcup_stickers.repositories.UserStickersRepository;
import com.leonardo.worldcup_stickers.repositories.UsersRepository;

@Service
public class StickersService {
    private static final int PACKAGE_SIZE = 7;
    private static final int COMMON_CHANCE = 60;
    private static final int RARE_CHANCE = 30;
    private static final BigDecimal PACKAGE_COST = BigDecimal.valueOf(7);

    private final StickersRepository stickersRepository;
    private final UserStickersRepository userStickersRepository;
    private final UsersRepository usersRepository;
    private final UserFinancialRepository userFinancialRepository;
    private final Random random = new Random();

    public StickersService(
            StickersRepository stickersRepository,
            UserStickersRepository userStickersRepository,
            UsersRepository usersRepository,
            UserFinancialRepository userFinancialRepository) {
        this.stickersRepository = stickersRepository;
        this.userStickersRepository = userStickersRepository;
        this.usersRepository = usersRepository;
        this.userFinancialRepository = userFinancialRepository;
    }

    @Transactional
    public List<StickerEntity> openPackage(Long userId) {
        UserFinancialEntity financial = userFinancialRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (financial.getCoins().compareTo(PACKAGE_COST) < 0) {
            throw new InsufficientCoinsException();
        }
        UserEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        financial.setCoins(financial.getCoins().subtract(PACKAGE_COST));
        List<StickerEntity> drawnStickers = new ArrayList<>();
        for (int i = 0; i < PACKAGE_SIZE; i++) {
            RarityEnum rarity = drawRarity();
            StickerEntity sticker = drawStickerByRarity(rarity);
            addToUserCollection(user, sticker);
            drawnStickers.add(sticker);
        }
        return drawnStickers;
    }

    private RarityEnum drawRarity() {
        int roll = random.nextInt(100);
        if (roll < COMMON_CHANCE) {
            return RarityEnum.COMMON;
        }
        if (roll < COMMON_CHANCE + RARE_CHANCE) {
            return RarityEnum.RARE;
        }
        return RarityEnum.LEGENDARY;
    }

    private StickerEntity drawStickerByRarity(RarityEnum rarity) {
        List<StickerEntity> pool = stickersRepository.findByRarity(rarity);
        return pool.get(random.nextInt(pool.size()));
    }

    private void addToUserCollection(UserEntity user, StickerEntity sticker) {
        UserStickerEntity userSticker = userStickersRepository
                .findByUserIdAndStickerId(user.getId(), sticker.getId())
                .orElseGet(() -> UserStickerEntity.builder()
                        .user(user)
                        .sticker(sticker)
                        .quantity(0)
                        .build());
        userSticker.setQuantity(userSticker.getQuantity() + 1);
        userStickersRepository.save(userSticker);
    }
}
