CREATE TABLE IF NOT EXISTS bucket
(
    id VARCHAR(255) PRIMARY KEY,
    state BYTEA
    --,expires_at BIGINT only on version 8.14.0
);