package com.leonardo.worldcup_stickers.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddBalanceDto(
                @NotNull(message = "amount must not be null") @Positive(message = "amount must be greater than zero") @Digits(integer = 17, fraction = 2, message = "amount must have at most 17 integer digits and 2 decimal places") BigDecimal amount) {
}
