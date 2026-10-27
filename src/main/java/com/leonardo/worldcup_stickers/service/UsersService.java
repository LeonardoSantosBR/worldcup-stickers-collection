package com.leonardo.worldcup_stickers.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.leonardo.worldcup_stickers.dto.UserCreateDto;
import com.leonardo.worldcup_stickers.entity.UserEntity;
import com.leonardo.worldcup_stickers.entity.UserCoinsEntity;
import com.leonardo.worldcup_stickers.entity.userTrade.UserTradeInventoryEntity;
import com.leonardo.worldcup_stickers.exception.EmailAlreadyExistsException;
import com.leonardo.worldcup_stickers.repository.UserCoinsRepository;
import com.leonardo.worldcup_stickers.repository.UserTradeInventoriesRepository;
import com.leonardo.worldcup_stickers.repository.UsersRepository;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserTradeInventoriesRepository userTradeInventoriesRepository;
    private final UserCoinsRepository userCoinsRepository;
    private final HashService hashService;
    private final S3Service s3Service;

    public UsersService(
            UsersRepository usersRepository,
            UserTradeInventoriesRepository userTradeInventoriesRepository,
            UserCoinsRepository userCoinsRepository,
            HashService hashService,
            S3Service s3Service) {
        this.usersRepository = usersRepository;
        this.userTradeInventoriesRepository = userTradeInventoriesRepository;
        this.userCoinsRepository = userCoinsRepository;
        this.hashService = hashService;
        this.s3Service = s3Service;
    }

    @Transactional
    public Long create(UserCreateDto body, MultipartFile avatar) {
        if (usersRepository.findByEmail(body.email()).isPresent())
            throw new EmailAlreadyExistsException(body.email());
        String avatarUrl = null;
        if (avatar != null && !avatar.isEmpty())
            avatarUrl = s3Service.upload(avatar, "avatars");
        UserEntity user = UserEntity
                .builder()
                .name(body.name())
                .email(body.email())
                .password(hashService.hash(body.password()))
                .avatarUrl(avatarUrl)
                .build();
        UserEntity savedUser = usersRepository.save(user);
        userTradeInventoriesRepository.save(
                UserTradeInventoryEntity
                        .builder()
                        .user(savedUser)
                        .build());
        userCoinsRepository.save(
                UserCoinsEntity
                        .builder()
                        .user(savedUser)
                        .build());
        return savedUser.getId();
    }

}
