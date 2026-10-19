package com.leonardo.worldcup_stickers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leonardo.worldcup_stickers.entity.UserEntity;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
