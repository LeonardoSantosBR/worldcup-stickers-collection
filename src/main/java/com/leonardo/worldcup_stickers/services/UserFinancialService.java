package com.leonardo.worldcup_stickers.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.worldcup_stickers.entities.UserFinancialEntity;
import com.leonardo.worldcup_stickers.exceptions.InsufficientBalanceException;
import com.leonardo.worldcup_stickers.exceptions.UserNotFoundException;
import com.leonardo.worldcup_stickers.repositories.UserFinancialRepository;

@Service
public class UserFinancialService {
    private final UserFinancialRepository userFinancialRepository;

    public UserFinancialService(UserFinancialRepository userFinancialRepository) {
        this.userFinancialRepository = userFinancialRepository;
    }

    @Transactional
    public boolean addBalance(Long userId, BigDecimal amount) {
        UserFinancialEntity financial = findFinancial(userId);
        financial.setMoney(financial.getMoney().add(amount));
        userFinancialRepository.save(financial);
        return true;
    }

    @Transactional
    public boolean addCoins(Long userId, BigDecimal amount) {
        UserFinancialEntity financial = findFinancial(userId);
        if (financial.getMoney().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
        financial.setMoney(financial.getMoney().subtract(amount));
        financial.setCoins(financial.getCoins().add(amount));
        userFinancialRepository.save(financial);
        return true;
    }

    private UserFinancialEntity findFinancial(Long userId) {
        return userFinancialRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
