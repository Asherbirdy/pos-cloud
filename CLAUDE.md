# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Layout

This directory is a monorepo-style container with two independent git repos:

- `pos-cloud-be/` — Spring Boot 3.1.5 backend (Java 17, Maven, PostgreSQL). Has its own `CLAUDE.md` with detailed conventions; **read it before editing backend code**.
- `pos-cloud-fe/` — Vue 3 + Vite + TypeScript admin frontend (naive-ui, Pinia, UnoCSS).

The two halves are developed and run separately. There is no top-level build.

### Directory tree

```
pos/
├── pos-cloud-be/                          # Spring Boot backend
│   ├── pom.xml
│   ├── postman/
│   └── src/
│       ├── main/
│       │   ├── java/com/app/security/
│       │   │   ├── aspect/                # @RequireStoreRole + StoreAccessAspect
│       │   │   ├── controller/            # Auth, Member, Enterprise, Store, StoreCheckout(Item), StoreProductCategory/Item, StoreShift, MemberStoreAccess
│       │   │   ├── dao/                   # interfaces + impl/ (NamedParameterJdbcTemplate)
│       │   │   ├── dto/                   # request/response DTOs grouped per domain
│       │   │   │   ├── Auth/  Member/  Enterprise/  Store/
│       │   │   │   ├── MemberStoreAccess/  StoreCheckout/  StoreCheckoutItem/
│       │   │   │   └── StoreProductCategory/  StoreProductItem/
│       │   │   ├── enums/                 # OrderStatus, ShiftStatus, StoreRole
│       │   │   ├── exception/             # GlobalExceptionHandler, ShiftLimitReachedException
│       │   │   ├── model/                 # domain entities (camelCase)
│       │   │   ├── rowmapper/             # snake_case → model bridges
│       │   │   ├── security/              # JwtAuthenticationFilter, JwtUtil, MySecurityConfig, RequestLoggingFilter
│       │   │   └── service/               # interfaces + impl/
│       │   └── resources/
│       └── test/
│           ├── java/com/app/security/{controller,service/impl,support}/
│           └── resources/                 # schema.sql, data.sql (H2 seed)
│
└── pos-cloud-fe/                          # Vue 3 + Vite frontend
    ├── index.html  vite.config.mts  uno.config.ts
    ├── tsconfig*.json  .eslintrc.cjs
    ├── public/
    └── src/
        ├── main.ts  App.vue  config.ts  theme.ts  env.d.ts
        ├── auto-imports.ts  auto-components.ts   # generated, do not edit
        ├── api/
        │   ├── http/                      # axios instance + interceptors
        │   ├── index.ts
        │   └── useAuthApi.ts
        ├── assets/scss/
        ├── components/{Layouts,…}
        ├── composable/
        ├── enum/                          # RequestRoute, etc.
        ├── layouts/default.vue            # vite-plugin-vue-layouts
        ├── pages/                         # vite-plugin-pages (file-based routes)
        ├── router/index.ts
        ├── stores/                        # Pinia (e.g. apps/productStore.ts)
        ├── types/{apis,apps,common}/
        └── utils/                         # cookie.ts, index.ts
```

## Backend (pos-cloud-be)

```bash
cd pos-cloud-be
mvn spring-boot:run            # run on port 8083 (reads .env)
mvn test                       # all tests (H2 in-memory)
mvn test -Dtest=ClassName#method  # single test
mvn clean package -DskipTests
```

See `pos-cloud-be/CLAUDE.md` for architecture (Controller → Service → DAO with Spring JDBC, no JPA), the **model / RowMapper / `schema.sql` three-way sync rule**, the SQL text-block convention, JWT auth flow, and the `member / enterprise / store / member_store_access` multi-tenancy model.

CORS: `MySecurityConfig.createCorsConfig()` 的 `setAllowedOrigins` 必須包含前端 dev server origin（預設 `http://localhost:1207`），否則前端打 API 會回 403 `Invalid CORS request`。

## Frontend (pos-cloud-fe)

```bash
cd pos-cloud-fe
pnpm i
pnpm dev          # vite dev server, default port 1207 (VITE_PORT)
pnpm build        # production build
pnpm type-check   # vue-tsc
pnpm lint         # eslint --fix
```

Env files: copy `.env.example` / `.env.development.example` / `.env.production.example` and fill `VITE_BASE`, `VITE_SERVER`, `VITE_PORT`. Backend base URL comes from `VITE_SERVER`.

### Frontend conventions

- **UI components**: Always use `naive-ui` components instead of native HTML tags (e.g. `<n-button>` not `<button>`, `<n-input>` not `<input>`, `<n-card>` not `<div>` for card-like containers).
- **Import style**: Always write multi-name imports on a single line (horizontal), not one per line. E.g. `import { NAvatar, NBadge, NButton } from 'naive-ui'`. Even with many names, keep them on one line — let the editor soft-wrap if needed. Do not split into multi-line `import { A, B, C } from '…'` blocks.
- **No native HTML tags**: `.vue` templates must not contain raw `<div>`, `<header>`, `<main>`, `<footer>`, `<section>`, `<span>`, `<p>`, `<h1>`–`<h6>` etc. Replace with their naive-ui equivalents:
  - Layout containers → `<n-layout>`, `<n-layout-header>`, `<n-layout-content>`, `<n-layout-footer>`, `<n-flex>`, `<n-grid>` / `<n-gi>`, `<n-space>`, or `<n-el>` for a generic themed box.
  - Headings → `<n-h1>` … `<n-h6>`.
  - Body text / inline text → `<n-text>` (use `depth` and `strong` props), or `<n-p>` for paragraphs.
  - Card-like containers → `<n-card>`.
  Only `<svg>` / `<path>` (inside `<n-icon>`) and similar non-layout primitives are exempt.
- **naive-ui imports**: naive-ui uses tree-shaking, so every naive-ui component must be explicitly imported into the `.vue` file that uses it (e.g. `import { NButton, NInput } from 'naive-ui'`). Do not rely on global registration.
- **Reactivity**: Always use `ref` for reactive state, never `reactive`. This applies even to object/form state (e.g. `const form = ref({ email: '', password: '' })`, accessed as `form.value.email` in script). Keep the codebase consistent with a single style.
- **Functions**: Always use arrow functions (`const fn = () => {}` / `const fn = async () => {}`) instead of `function` declarations in `.vue` files and frontend `.ts` files. Keep a single consistent style across the codebase.
- **Page state**: Every `.vue` file groups its reactive state into a single `state` ref with two buckets:
  ```ts
  const state = ref({
    data: {},     // page data (form fields, API results, displayed entities)
    feature: {}   // UI/feature flags (loading, dialog open, rules, pagination, etc.)
  })
  ```
  Access in script as `state.value.data.xxx` / `state.value.feature.xxx`, in template as `state.data.xxx` / `state.feature.xxx`. Template refs (e.g. `formRef`) stay outside `state`.

### Frontend architecture notes

- **File-based routing** via `vite-plugin-pages` over `src/pages/` plus `vite-plugin-vue-layouts` over `src/layouts/`. Adding a `.vue` file under `src/pages/` creates a route automatically — do not hand-edit a route table. `src/router/index.ts` wires the generated routes and guards.
- **Auto-imports** via `unplugin-auto-import` (Vue, vue-router, selected naive-ui composables) and `unplugin-vue-components` (Vue components). Generated declaration files `src/auto-imports.ts` and `src/auto-components.ts` are committed and regenerated by Vite — do not edit by hand, and do not add manual `import` statements for things that are already auto-imported.
- **Path alias**: `@/` → `src/`.
- **Styling**: UnoCSS with `presetUno` + `presetAttributify` (see `uno.config.ts`); attributify syntax (e.g. `<div text="sm gray-500">`) is enabled.
- **API layer**: `src/api/http` (axios wrapper) + per-feature modules like `src/api/useAuthApi.ts`. State in `src/stores/` (Pinia).
- **Response shape**: 後端統一回傳 `{ msg, data }`，前端用共用型別 `ApiResponse<T>`（`src/types/common/ApiResponse.ts`）。API 模組型別寫 `AxiosPromise<ApiResponse<TPayload>>`；**不**在 interceptor 統一拆包，呼叫端（`.vue` / composable / store）自己取 `res.data.data`，例如 `const res = await xxxApi.yyy(); const payload = res.data.data`。
- **Server state / mutations**: 用 `@tanstack/vue-query` 的 `useQuery` / `useMutation` 包 API 呼叫；不要在 `.vue` 內直接 `await` API 並自己維護 `loading` flag。loading 綁 `mutation.isPending.value`，成功/失敗在 `onSuccess` / `onError` 處理。
- **API URL enum**: 所有 API URL 統一寫進 `src/enum/RequestRoute.ts`，依後端 `MySecurityConfig` 的權限分成兩個 enum：
  - `PublicApiRoute` — 後端 `permitAll` 路徑（`/auth/**`、`/dev/test`）。
  - `AuthApiRoute` — 需要登入或角色驗證的路徑（`/member/**`、`/enterprise/**`、`/member-store-access/**`、`/storeShift/**`、`/product-category/**`、`/product-item/**`、`/store/`、`/checkout/` 等）。
  Enum 命名照 API 路徑（`/auth/login` → `AuthLogin`、`/member-store-access/` → `MemberStoreAccess`）。靜態路徑直接用 enum 值，含 path param 時用 template literal 組合（例如 `` `${AuthApiRoute.Enterprise}${enterpriseId}` ``）。新增 API 時要先把 URL 補到對應 enum 再使用。
- **Auth flow（前後端分離）**:
  - 前後端透過 CORS 跨網域；前端 dev server 在 `http://localhost:1207`，後端在 `http://localhost:8083`。
  - 登入成功時後端在 `AuthLoginResponse.tokenPair` 回傳 `{ accessToken, refreshToken }`；前端把這兩個值寫入 cookie（`CookieEnum.AccessToken` / `CookieEnum.RefreshToken`）。
  - 後續請求由 `src/api/http/axios/Axios.ts` 的 request interceptor 從 cookie 讀 `accessToken`，掛 `Authorization: Bearer <token>` header。
  - accessToken 不存在時自動打 `/auth/refreshToken`（用 refreshToken）換新 accessToken。**`PublicApiRoute` 內的 URL 不會走 refresh 流程也不掛 Authorization**，避免登入/註冊頁無 token 時被多打一次 refresh。
  - cookie 由前端 JS（`utils/cookie.ts`，`js-cookie`）讀寫，**不是** HttpOnly。`axios` 的 `withCredentials` 維持 `false`，因為 auth 走 Bearer header，不靠 cookie 傳輸。
