# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
mvn clean package              # Build project
mvn spring-boot:run            # Run application (port 8083)
mvn test                       # Run all tests
mvn clean package -DskipTests  # Build without tests
```

- Requires Java 17 and PostgreSQL
- Uses spring-dotenv: database config loaded from `.env` file (see `.envSample` for template)
- Test database: H2 in-memory (no external DB needed for tests)

## Architecture

Spring Boot 3.1.5 app with layered architecture using Spring JDBC (not JPA):

```
Controller → Service (interface + impl) → DAO (interface + impl) → PostgreSQL
```

All code lives under `com.app.security`. DAO layer uses `NamedParameterJdbcTemplate` with raw SQL and `RowMapper` classes for result mapping.

### Authentication & Security

- Stateless JWT auth with HttpOnly cookie-based token transport
- Access token (15 min) + refresh token (24 hr) stored in `token` table with IP/User-Agent tracking
- `JwtAuthenticationFilter` auto-refreshes expired access tokens using valid refresh tokens
- Route security in `MySecurityConfig`: `/auth/**` is public, `/enterprise/**` requires admin role, `/member/**` requires authentication
- Passwords hashed with BCrypt

### Multi-Tenancy Model

- `member` → system-level users with global role (admin/user)
- `enterprise` → organizations
- `store` → retail locations under an enterprise
- `member_store_access` → per-store role assignment (STORE_MANAGER, STORE_STAFF) with status tracking
- One member can have access to multiple enterprises/stores

### Response Pattern

All endpoints return `Response<T>` which wraps `ApiResponse<T>` (message + data) in a `ResponseEntity`. Errors handled by `GlobalExceptionHandler`.

## Testing

- Integration tests use `@SpringBootTest` + `MockMvc` with H2 database
- `AuthTestSupport` base class provides pre-authenticated `adminAccessToken` and `userAccessToken` helpers
- Test data seeded via `src/test/resources/data.sql` (admin@gmail.com / user@gmail.com, password: "password")
- Test suite runner: `ApplicationTest.java` using JUnit 5 Platform Suite

## Roles

System roles: `admin`, `user` (stored in `member.role`)
Store-level roles: `store_manager`, `store_staff` (stored in `member_store_access.role`)
