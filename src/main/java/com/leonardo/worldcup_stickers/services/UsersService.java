package com.leonardo.worldcup_stickers.services;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.worldcup_stickers.dto.MyProfileDto;
import com.leonardo.worldcup_stickers.dto.MyStickerDto;
import com.leonardo.worldcup_stickers.dto.PageResponseDto;
import com.leonardo.worldcup_stickers.entities.UserFinancialEntity;
import com.leonardo.worldcup_stickers.entities.UserEntity;
import com.leonardo.worldcup_stickers.entities.UserStickerEntity;
import com.leonardo.worldcup_stickers.entities.UserTradeInventoryEntity;
import com.leonardo.worldcup_stickers.exceptions.EmailAlreadyExistsException;
import com.leonardo.worldcup_stickers.exceptions.InsufficientBalanceException;
import com.leonardo.worldcup_stickers.exceptions.UserNotFoundException;
import com.leonardo.worldcup_stickers.repositories.StickersRepository;
import com.leonardo.worldcup_stickers.repositories.UserFinancialRepository;
import com.leonardo.worldcup_stickers.repositories.UserStickersRepository;
import com.leonardo.worldcup_stickers.repositories.UserTradeInventoriesRepository;
import com.leonardo.worldcup_stickers.repositories.UsersRepository;

@Service
public class UsersService {
    private static final int MAX_LIMIT = 100;

    private final UsersRepository usersRepository;
    private final UserStickersRepository userStickersRepository;
    private final UserTradeInventoriesRepository userTradeInventoriesRepository;
    private final UserFinancialRepository userFinancialRepository;
    private final StickersRepository stickersRepository;
    private final HashService hashService;

    public UsersService(
            UsersRepository usersRepository,
            UserStickersRepository userStickersRepository,
            UserTradeInventoriesRepository userTradeInventoriesRepository,
            UserFinancialRepository userFinancialRepository,
            StickersRepository stickersRepository,
            HashService hashService) {
        this.usersRepository = usersRepository;
        this.userStickersRepository = userStickersRepository;
        this.userTradeInventoriesRepository = userTradeInventoriesRepository;
        this.userFinancialRepository = userFinancialRepository;
        this.stickersRepository = stickersRepository;
        this.hashService = hashService;
    }

    @Transactional
    public boolean create(UserEntity user) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        String hashedPassword = hashService.hash(user.getPassword());
        user.setPassword(hashedPassword);
        UserEntity savedUser = usersRepository.save(user);

        userTradeInventoriesRepository.save(
                UserTradeInventoryEntity.builder()
                        .user(savedUser)
                        .build());

        userFinancialRepository.save(
                UserFinancialEntity.builder()
                        .user(savedUser)
                        .build());

        return true;
    }

    @Transactional
    public boolean addBalance(Long userId, BigDecimal amount) {
        UserFinancialEntity financial = userFinancialRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        financial.setMoney(financial.getMoney().add(amount));
        userFinancialRepository.save(financial);
        return true;
    }

    @Transactional
    public boolean addCoins(Long userId, BigDecimal amount) {
        UserFinancialEntity financial = userFinancialRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (financial.getMoney().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
        financial.setMoney(financial.getMoney().subtract(amount));
        financial.setCoins(financial.getCoins().add(amount));
        userFinancialRepository.save(financial);
        return true;
    }

    @Transactional(readOnly = true)
    public MyProfileDto findMyUser(Long userId) {
        UserEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        long ownedStickers = userStickersRepository.countByUserId(userId);
        long totalStickers = stickersRepository.count();

        return MyProfileDto.fromEntity(user, ownedStickers, totalStickers);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<MyStickerDto> findMyStickers(Long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(
                Math.max(page - 1, 0),
                Math.min(Math.max(limit, 1), MAX_LIMIT),
                Sort.by("sticker.number").ascending());

        Page<UserStickerEntity> result = userStickersRepository.findByUserId(userId, pageable);
        return PageResponseDto.from(result, MyStickerDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<MyStickerDto> findMyStickersAvailableTrade(Long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(
                Math.max(page - 1, 0),
                Math.min(Math.max(limit, 1), MAX_LIMIT),
                Sort.by("sticker.number").ascending());

        List<Long> availableStickerIds = userTradeInventoriesRepository.findByUserId(userId)
                .map(UserTradeInventoryEntity::getAvailableStickerIds)
                .orElseGet(List::of);

        if (availableStickerIds.isEmpty()) {
            return PageResponseDto.from(Page.<UserStickerEntity>empty(pageable), MyStickerDto::fromEntity);
        }

        Page<UserStickerEntity> result = userStickersRepository.findByUserIdAndStickerIdIn(
                userId, availableStickerIds, pageable);
        return PageResponseDto.from(result, MyStickerDto::fromEntity);
    }
}
