# Backend (Spring Boot)

## Overview
- Java REST API using Spring Boot
- Supabase integration for authentication and database

## Getting Started
1. Install Java 17+ and Gradle (or use Maven)
2. Run: `./gradlew bootRun` or `./mvnw spring-boot:run`
3. API runs at `http://localhost:8080`

## Structure
- `controller/` - REST endpoints
- `service/` - Business logic
- `model/` - Data models

## Configuration
- Edit `src/main/resources/application.properties` for Supabase/Postgres connection.
