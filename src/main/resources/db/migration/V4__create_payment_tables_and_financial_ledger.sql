CREATE TABLE payment_order (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    package_code VARCHAR(100) NOT NULL,
    amount_cents BIGINT NOT NULL CHECK (amount_cents > 0),
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    checkout_session_id VARCHAR(255) UNIQUE,
    payment_intent_id VARCHAR(255) UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT chk_payment_order_status
        CHECK (status IN ('PENDING', 'PAID', 'FAILED', 'CANCELLED'))
);

CREATE INDEX idx_payment_order_user_created
    ON payment_order (user_id, created_at DESC);

CREATE TABLE payment_webhook_event (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(255) NOT NULL UNIQUE,
    event_type VARCHAR(255) NOT NULL,
    received_at TIMESTAMP NOT NULL,
    processing_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT chk_payment_webhook_event_processing_status
        CHECK (processing_status IN ('PENDING', 'PROCESSING', 'PROCESSED', 'FAILED'))
);

CREATE TABLE user_financial_ledger (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    payment_order_id BIGINT REFERENCES payment_order(id),
    movement_type VARCHAR(30) NOT NULL,
    currency_type VARCHAR(10) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_user_financial_ledger_movement_type
        CHECK (movement_type IN ('CREDIT', 'CONVERSION')),
    CONSTRAINT chk_user_financial_ledger_currency_type
        CHECK (currency_type IN ('MONEY', 'COINS'))
);

CREATE INDEX idx_user_financial_ledger_user_created
    ON user_financial_ledger (user_id, created_at DESC);

CREATE INDEX idx_user_financial_ledger_payment_order
    ON user_financial_ledger (payment_order_id);
