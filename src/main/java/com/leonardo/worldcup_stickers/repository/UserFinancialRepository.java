package com.leonardo.worldcup_stickers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leonardo.worldcup_stickers.entity.UserFinancialEntity;

public interface UserFinancialRepository extends JpaRepository<UserFinancialEntity, Long> {
    java.util.Optional<UserFinancialEntity> findByUserId(Long userId);
}
