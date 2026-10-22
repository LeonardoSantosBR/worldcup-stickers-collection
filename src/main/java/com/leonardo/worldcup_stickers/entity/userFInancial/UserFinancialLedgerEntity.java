package com.leonardo.worldcup_stickers.entity.userFInancial;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.leonardo.worldcup_stickers.enums.ledger.LedgerCurrencyType;
import com.leonardo.worldcup_stickers.enums.ledger.LedgerMovementType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_financial_ledger")
@Getter
@Setter
public class UserFinancialLedgerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "payment_order_id")
    private Long paymentOrderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private LedgerMovementType movementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type", nullable = false, length = 10)
    private LedgerCurrencyType currencyType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}