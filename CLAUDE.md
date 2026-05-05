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

See `pos-cloud-be/CLAUDE.md` for architecture (Controller → Service → DAO with Spring JDBC, no JPA), the **model / RowMapper / `schema.sql` three-way sync rule**, the SQL text-block convention, JWT cookie auth flow, and the `member / enterprise / store / member_store_access` multi-tenancy model.

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
- **naive-ui imports**: naive-ui uses tree-shaking, so every naive-ui component must be explicitly imported into the `.vue` file that uses it (e.g. `import { NButton, NInput } from 'naive-ui'`). Do not rely on global registration.
- **Reactivity**: Always use `ref` for reactive state, never `reactive`. This applies even to object/form state (e.g. `const form = ref({ email: '', password: '' })`, accessed as `form.value.email` in script). Keep the codebase consistent with a single style.
- **Functions**: Always use arrow functions (`const fn = () => {}` / `const fn = async () => {}`) instead of `function` declarations in `.vue` files and frontend `.ts` files. Keep a single consistent style across the codebase.

### Frontend architecture notes

- **File-based routing** via `vite-plugin-pages` over `src/pages/` plus `vite-plugin-vue-layouts` over `src/layouts/`. Adding a `.vue` file under `src/pages/` creates a route automatically — do not hand-edit a route table. `src/router/index.ts` wires the generated routes and guards.
- **Auto-imports** via `unplugin-auto-import` (Vue, vue-router, selected naive-ui composables) and `unplugin-vue-components` (Vue components). Generated declaration files `src/auto-imports.ts` and `src/auto-components.ts` are committed and regenerated by Vite — do not edit by hand, and do not add manual `import` statements for things that are already auto-imported.
- **Path alias**: `@/` → `src/`.
- **Styling**: UnoCSS with `presetUno` + `presetAttributify` (see `uno.config.ts`); attributify syntax (e.g. `<div text="sm gray-500">`) is enabled.
- **API layer**: `src/api/http` (axios wrapper) + per-feature modules like `src/api/useAuthApi.ts`. State in `src/stores/` (Pinia).
- Auth tokens travel as HttpOnly cookies set by the backend; the frontend should not read/write the JWT directly.
