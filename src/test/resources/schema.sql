CREATE TABLE IF NOT EXISTS member
(
    member_id  VARCHAR(36) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(50) DEFAULT 'user',
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS token
(
    token_id      VARCHAR(36) PRIMARY KEY,
    refresh_token VARCHAR(255) NOT NULL,
    ip            VARCHAR(50),
    user_agent    VARCHAR(500),
    is_valid      BOOLEAN   DEFAULT TRUE,
    member_id     VARCHAR(36)  NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_token_member
        FOREIGN KEY (member_id)
            REFERENCES member (member_id)
);

CREATE table IF NOT EXISTS enterprise
(
    enterprise_id VARCHAR(64) PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS store
(
    store_id              VARCHAR(64) PRIMARY KEY,
    enterprise_id         VARCHAR(64)  NOT NULL,
    name                  VARCHAR(255) NOT NULL,
    running_devices_limit INT       DEFAULT 1,
    is_active             BOOLEAN   DEFAULT TRUE,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_store_enterprise
        FOREIGN KEY (enterprise_id)
            REFERENCES enterprise (enterprise_id)
            ON DELETE CASCADE
);