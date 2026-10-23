package com.leonardo.worldcup_stickers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leonardo.worldcup_stickers.entity.UserCoinsEntity;

public interface UserCoinsRepository extends JpaRepository<UserCoinsEntity, Long> {
    java.util.Optional<UserCoinsEntity> findByUserId(Long userId);
}
