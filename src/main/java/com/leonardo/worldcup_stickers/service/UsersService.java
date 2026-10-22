package com.leonardo.worldcup_stickers.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.worldcup_stickers.dto.UserCreateDto;
import com.leonardo.worldcup_stickers.entity.UserEntity;
import com.leonardo.worldcup_stickers.entity.userFInancial.UserFinancialEntity;
import com.leonardo.worldcup_stickers.entity.userTrade.UserTradeInventoryEntity;
import com.leonardo.worldcup_stickers.exception.EmailAlreadyExistsException;
import com.leonardo.worldcup_stickers.repository.UserFinancialRepository;
import com.leonardo.worldcup_stickers.repository.UserTradeInventoriesRepository;
import com.leonardo.worldcup_stickers.repository.UsersRepository;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserTradeInventoriesRepository userTradeInventoriesRepository;
    private final UserFinancialRepository userFinancialRepository;
    private final HashService hashService;

    public UsersService(
            UsersRepository usersRepository,
            UserTradeInventoriesRepository userTradeInventoriesRepository,
            UserFinancialRepository userFinancialRepository,
            HashService hashService) {
        this.usersRepository = usersRepository;
        this.userTradeInventoriesRepository = userTradeInventoriesRepository;
        this.userFinancialRepository = userFinancialRepository;
        this.hashService = hashService;
    }

    @Transactional
    public Long create(UserCreateDto body) {
        if (usersRepository.findByEmail(body.email()).isPresent()) {
            throw new EmailAlreadyExistsException(body.email());
        }
        UserEntity user = UserEntity.builder()
                .name(body.name())
                .email(body.email())
                .password(hashService.hash(body.password()))
                .build();
        UserEntity savedUser = usersRepository.save(user);
        userTradeInventoriesRepository.save(
                UserTradeInventoryEntity.builder()
                        .user(savedUser)
                        .build());
        userFinancialRepository.save(
                UserFinancialEntity.builder()
                        .user(savedUser)
                        .build());
        return savedUser.getId();
    }

}
