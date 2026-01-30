# Database Workflow: Migrations & Type-Safe Generation

This document outlines the workflow for managing the database schema (PostgreSQL) and generating type-safe Java code using JOOQ.

## 1. Prerequisites

Ensure you have the local database running via Docker Compose:

```bash
docker compose up -d
```

This starts a PostgreSQL container named `vekku-spring-postgres` on port `5432` with database `vekku_db`.

## 2. Creating Migrations (Flyway)

We use **Flyway** for database migrations. All migration scripts are located in:
`src/main/resources/db/migration`

### Naming Convention
Files must follow the standard Flyway naming convention:
`V<VERSION>__<DESCRIPTION>.sql`

*   **Version:** detailed timestamp or sequential number (e.g., `1`, `1.1`, `202310271030`)
*   **Separator:** Two underscores (`__`)
*   **Description:** Snake case description (e.g., `create_users_table`)

**Example:**
`V1__create_initial_schema.sql`

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
```

## 3. Applying Migrations

When you run the Spring Boot application, Flyway automatically applies pending migrations to the configured database.

```bash
./gradlew bootRun
```

Or you can run the Flyway task explicitly (if configured) or just let the app startup handle it.

## 4. Generating Type-Safe Code (JOOQ)

After your database schema is updated (via migrations), you need to regenerate the JOOQ classes to reflect these changes in your Java code.

### The Command
Run the following Gradle task:

```bash
./gradlew jooqCodegen
```

### What happens?
1.  Gradle connects to the local database (`jdbc:postgresql://localhost:5432/vekku_db`).
2.  It reads the current schema (tables, columns, types).
3.  It generates Java classes in `build/generated-sources/jooq`.
    *   **Package:** `dev.kuku.vekku.jooq`

### Using Generated Code
You can now use these classes in your services for type-safe SQL queries.

**Example:**
```java
import static dev.kuku.vekku.jooq.Tables.USERS;

// ... inside a service
dslContext.selectFrom(USERS)
          .where(USERS.USERNAME.eq("john_doe"))
          .fetchOne();
```

## Summary Workflow

1.  **Create Migration:** Add `V{N}__description.sql` to `src/main/resources/db/migration`.
2.  **Apply Migration:** Run the app (`./gradlew bootRun`) to apply the SQL to the DB.
3.  **Generate Code:** Run `./gradlew jooqCodegen` to update Java classes.
4.  **Develop:** Write code using the updated JOOQ classes.
