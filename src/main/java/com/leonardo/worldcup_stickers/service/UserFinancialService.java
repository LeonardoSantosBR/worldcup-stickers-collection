package com.leonardo.worldcup_stickers.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.worldcup_stickers.entity.UserCoinsEntity;
import com.leonardo.worldcup_stickers.exception.UserNotFoundException;
import com.leonardo.worldcup_stickers.repository.UserCoinsRepository;

@Service
public class UserFinancialService {
    private final UserCoinsRepository userCoinsRepository;

    public UserFinancialService(UserCoinsRepository userCoinsRepository) {
        this.userCoinsRepository = userCoinsRepository;
    }

    @Transactional
    public boolean addCoins(Long userId, BigDecimal amount) {
        UserCoinsEntity financial = findFinancial(userId);
        financial.setCoins(financial.getCoins().add(amount));
        userCoinsRepository.save(financial);
        return true;
    }

    private UserCoinsEntity findFinancial(Long userId) {
        return userCoinsRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
