# pos-cloud

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
- MemberStoreAccess 可以看自己有哪些權限


### 開班
- 每個store都有自己獨立的帳號
- STAFF 登入帳號 
- STAFF 開班 (並記錄LOG)
- STAFF 關班 (並記錄LOG)