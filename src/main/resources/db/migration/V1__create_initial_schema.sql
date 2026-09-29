CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE stickers (
    id BIGSERIAL PRIMARY KEY,
    number INTEGER NOT NULL UNIQUE,
    player_name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    sticker_group VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    rarity VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE user_stickers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    sticker_id BIGINT NOT NULL REFERENCES stickers(id),
    quantity INTEGER NOT NULL,
    CONSTRAINT uk_user_stickers_user_id_sticker_id UNIQUE (user_id, sticker_id)
);

CREATE TABLE user_trade_inventories (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    available_sticker_ids BIGINT[] NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE user_trade_offers (
    id BIGSERIAL PRIMARY KEY,
    proposer_id BIGINT NOT NULL REFERENCES users(id),
    receiver_id BIGINT NOT NULL REFERENCES users(id),
    requested_sticker_ids BIGINT[] NOT NULL,
    offered_sticker_ids BIGINT[] NOT NULL,
    status VARCHAR(20) NOT NULL,
    message VARCHAR(255),
    responded_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_trade_offers_receiver_status
    ON user_trade_offers (receiver_id, status);
CREATE INDEX idx_trade_offers_proposer_status
    ON user_trade_offers (proposer_id, status);

CREATE TABLE user_trade_offers_logs (
    id BIGSERIAL PRIMARY KEY,
    trade_offer_id BIGINT NOT NULL REFERENCES user_trade_offers(id),
    status VARCHAR(20) NOT NULL,
    changed_by_user_id BIGINT NOT NULL REFERENCES users(id),
    note VARCHAR(255),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_trade_offer_logs_offer
    ON user_trade_offers_logs (trade_offer_id);
