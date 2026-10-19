package com.leonardo.worldcup_stickers.exception;

import org.springframework.http.HttpStatus;

import com.leonardo.worldcup_stickers.config.ApiException;

public class TradeOfferNotFoundException extends ApiException {
    public TradeOfferNotFoundException(Long offerId) {
        super(HttpStatus.NOT_FOUND, "Trade offer not found: " + offerId);
    }
}
