# Project Context: Vekku Spring

## Project Overview
**Vekku Spring** is a backend service built with **Spring Boot** and **Java 21**, managed by **Gradle**. It serves as a robust API layer utilizing **GraphQL** (Netflix DGS & Spring GraphQL) and interfaces with a **PostgreSQL** database.

## Key Technologies
*   **Language:** Java 21
*   **Framework:** Spring Boot 4.0.2
*   **Build System:** Gradle (Kotlin DSL)
*   **Database:** PostgreSQL
    *   **ORM/Access:** Spring Data JPA, JOOQ
    *   **Migration:** Flyway
*   **API:** GraphQL
    *   **Libraries:** Netflix DGS, Spring GraphQL
*   **Security:** Spring Security
*   **Utilities:** Lombok

## Architecture & Structure
The project follows a standard Spring Boot directory structure:
*   `src/main/java`: Source code (Root package: `dev.kuku.vekku`)
*   `src/main/resources`: Configuration and static resources.
    *   `application.properties`: Main application configuration.
    *   `db/migration`: Flyway SQL migration scripts.
    *   `graphql`: GraphQL schema definitions.
*   `src/test/java`: Test suite (JUnit 5).

## Building and Running

### Prerequisites
*   Java 21
*   Docker (optional, for database if not running locally)

### Key Commands
Use the included Gradle wrapper (`./gradlew` on Unix-like, `gradlew.bat` on Windows):

*   **Build Project:**
    ```bash
    ./gradlew build
    ```
*   **Run Application:**
    ```bash
    ./gradlew bootRun
    ```
*   **Run Tests:**
    ```bash
    ./gradlew test
    ```
*   **Clean Build:**
    ```bash
    ./gradlew clean build
    ```

## Development Conventions
*   **Code Style:** Standard Java conventions.
*   **Database:** 
    *   Schema changes should be managed via Flyway migrations in `src/main/resources/db/migration`.
    *   JPA is used for standard entity management, while JOOQ is available for type-safe complex queries.
*   **API:** The API is primarily GraphQL-driven. Schema changes should be reflected in the `src/main/resources/graphql` directory.
