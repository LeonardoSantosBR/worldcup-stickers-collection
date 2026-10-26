package com.leonardo.worldcup_stickers.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.worldcup_stickers.config.JwtAuthFilter;
import com.leonardo.worldcup_stickers.dto.MakeOfferDto;
import com.leonardo.worldcup_stickers.dto.PageResponseDto;
import com.leonardo.worldcup_stickers.dto.RespondOfferDto;
import com.leonardo.worldcup_stickers.dto.tradeOffer.TradeOfferDetailDto;
import com.leonardo.worldcup_stickers.dto.tradeOffer.TradeOfferDto;
import com.leonardo.worldcup_stickers.enums.TradeStatusEnum;
import com.leonardo.worldcup_stickers.service.UserTradeOffersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Trade offers", description = "Create, review, and respond to sticker trade offers")
@RequestMapping("/user-trade-offers")
public class UserTradeOffersController {
    private final UserTradeOffersService userTradeOffersService;

    public UserTradeOffersController(UserTradeOffersService userTradeOffersService) {
        this.userTradeOffersService = userTradeOffersService;
    }

    @GetMapping("/inbox")
    @Operation(summary = "List received offers", description = "Returns a paginated inbox of offers received by the authenticated user, optionally filtered by status.")
    public PageResponseDto<TradeOfferDetailDto> inbox(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) TradeStatusEnum status) {
        return userTradeOffersService.findReceivedOffers(userId, page, limit, status);
    }

    @GetMapping("/outbox")
    @Operation(summary = "List sent offers", description = "Returns a paginated outbox of offers sent by the authenticated user, optionally filtered by status.")
    public PageResponseDto<TradeOfferDetailDto> outbox(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) TradeStatusEnum status) {
        return userTradeOffersService.findSentOffers(userId, page, limit, status);
    }

    @PostMapping("/make-offer")
    @Operation(summary = "Create a trade offer", description = "Creates a pending offer to exchange the specified stickers with another user.")
    @ResponseStatus(HttpStatus.CREATED)
    public TradeOfferDto makeOffer(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @Valid @RequestBody MakeOfferDto body) {
        return userTradeOffersService.makeOffer(userId, body);
    }

    @PostMapping("/{offerId}/accept")
    @Operation(summary = "Accept a trade offer", description = "Accepts a received pending offer and transfers the offered and requested stickers.")
    public boolean acceptOffer(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @PathVariable Long offerId,
            @Valid @RequestBody(required = false) RespondOfferDto body) {
        return userTradeOffersService.acceptOffer(userId, offerId, noteOf(body));
    }

    @PostMapping("/{offerId}/reject")
    @Operation(summary = "Reject a trade offer", description = "Rejects a received pending offer without transferring stickers.")
    public boolean rejectOffer(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @PathVariable Long offerId,
            @Valid @RequestBody(required = false) RespondOfferDto body) {
        return userTradeOffersService.rejectOffer(userId, offerId, noteOf(body));
    }

    @PostMapping("/{offerId}/cancel")
    @Operation(summary = "Cancel a trade offer", description = "Cancels a pending offer created by the authenticated user.")
    public boolean cancelOffer(
            @RequestAttribute(JwtAuthFilter.USER_ID_ATTRIBUTE) Long userId,
            @PathVariable Long offerId,
            @Valid @RequestBody(required = false) RespondOfferDto body) {
        return userTradeOffersService.cancelOffer(userId, offerId, noteOf(body));
    }

    private static String noteOf(RespondOfferDto body) {
        return body == null ? null : body.note();
    }
}
