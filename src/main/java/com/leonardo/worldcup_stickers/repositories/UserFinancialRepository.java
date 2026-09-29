package com.leonardo.worldcup_stickers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leonardo.worldcup_stickers.entities.UserFinancialEntity;

public interface UserFinancialRepository extends JpaRepository<UserFinancialEntity, Long> {
}
