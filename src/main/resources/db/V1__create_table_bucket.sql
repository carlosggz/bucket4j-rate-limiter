CREATE TABLE IF NOT EXISTS bucket
(
    id VARCHAR(255) PRIMARY KEY,
    state BYTEA,
    expires_at BIGINT,
    lock BIGINT
);