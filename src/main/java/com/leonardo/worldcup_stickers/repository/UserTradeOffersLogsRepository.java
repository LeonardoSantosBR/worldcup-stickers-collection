package com.leonardo.worldcup_stickers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leonardo.worldcup_stickers.entity.userTrade.UserTradeOffersLogsEntity;

public interface UserTradeOffersLogsRepository extends JpaRepository<UserTradeOffersLogsEntity, Long> {

    List<UserTradeOffersLogsEntity> findByTradeOfferIdOrderByCreatedAtAsc(Long tradeOfferId);
}
