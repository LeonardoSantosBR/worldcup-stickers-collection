package com.leonardo.worldcup_stickers.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leonardo.worldcup_stickers.entities.UserEntity;
import com.leonardo.worldcup_stickers.entities.UserStickerEntity;
import com.leonardo.worldcup_stickers.entities.UserTradeInventoryEntity;

public interface MyProfileRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT COUNT(us) FROM UserStickerEntity us WHERE us.user.id = :userId")
    long countOwnedStickersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(s) FROM StickerEntity s")
    long countTotalStickers();

    @Query(value = "SELECT us FROM UserStickerEntity us WHERE us.user.id = :userId",
            countQuery = "SELECT COUNT(us) FROM UserStickerEntity us WHERE us.user.id = :userId")
    @EntityGraph(attributePaths = "sticker")
    Page<UserStickerEntity> findStickersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT us FROM UserStickerEntity us WHERE us.user.id = :userId AND us.sticker.id IN :stickerIds",
            countQuery = "SELECT COUNT(us) FROM UserStickerEntity us WHERE us.user.id = :userId AND us.sticker.id IN :stickerIds")
    @EntityGraph(attributePaths = "sticker")
    Page<UserStickerEntity> findStickersByUserIdAndStickerIdIn(
            @Param("userId") Long userId, @Param("stickerIds") java.util.Collection<Long> stickerIds, Pageable pageable);

    @Query("SELECT inventory FROM UserTradeInventoryEntity inventory WHERE inventory.user.id = :userId")
    Optional<UserTradeInventoryEntity> findTradeInventoryByUserId(@Param("userId") Long userId);
}
