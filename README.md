# pos-cloud

#### TABLES

```sql
CREATE TABLE member
(
    member_id  VARCHAR(36) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(50) DEFAULT 'user',
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE token
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

CREATE table enterprise
(
    enterprise_id VARCHAR(64) PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE store
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

CREATE TABLE member_store_access
(
    id            VARCHAR(36) PRIMARY KEY,
    member_id     VARCHAR(36) NOT NULL,
    enterprise_id VARCHAR(64) NOT NULL,
    store_id      VARCHAR(64) NOT NULL,
    role          VARCHAR(50) DEFAULT 'STORE_STAFF', -- 可選：每個 store 有不同角色                                                                                                          
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_msa_member FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT fk_msa_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprise (enterprise_id),
    CONSTRAINT fk_msa_store FOREIGN KEY (store_id) REFERENCES store (store_id),
    CONSTRAINT uk_member_store UNIQUE (member_id, store_id)
);

```

### Roles
- ADMIN
- MANAGER
- STAFF

## STORE_ROLE
- STORE_MANAGER
- STORE_STAFF

### 啟動專案流程
- 創建TABLE至資料庫
- 打DEV_REGISTER 建立ADMIN帳號 member role=ADMIN 才能建立企業帳號

### 企業開戶流程
- ADMIN 幫忙申請 ENTERPRISE
- ADMIN 幫忙 ENTERPRISE 申請 STORE
- ADMIN 幫忙 STORE 申請 STORE_MANAGER 帳號
- STORE_MANAGER 幫忙 STORE_STAFF申請 開班帳號

### 開店流程
- 開發者請 MANAGER 登入POS系統（需要EMAIL驗證）
- MANAGER 設定 STORE 可以同時開班的次數
- MANAGER 幫忙 STAFF創建帳號

### 開班
- 每個store都有自己獨立的帳號
- STAFF 登入帳號 
- STAFF 開班 (並記錄LOG)
- STAFF 關班 (並記錄LOG)