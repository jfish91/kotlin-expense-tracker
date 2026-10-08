# Kotlin Expense Tracker

A REST API for tracking personal expenses, built as a hands-on project to learn Kotlin, Spring Boot, and the Java/Spring ecosystem.

## Tech Stack

* Kotlin
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Gradle

## Current Features

* Create, retrieve, update, and delete expenses
* Retrieve individual expenses by UUID
* Request validation
* Global exception handling
* Custom handling for missing expenses
* PostgreSQL persistence
* Automatically generated UUIDs
* Automatically generated creation timestamps

## API Endpoints

| Method   | Endpoint         | Description                 |
| -------- | ---------------- | --------------------------- |
| `POST`   | `/expenses`      | Create a new expense        |
| `GET`    | `/expenses`      | Retrieve all expenses       |
| `GET`    | `/expenses/{id}` | Retrieve an expense by UUID |
| `PUT`    | `/expenses/{id}` | Update an existing expense  |
| `DELETE` | `/expenses/{id}` | Delete an expense           |

## Architecture

```text
HTTP Request
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
Spring Data JPA
    ↓
Hibernate
    ↓
PostgreSQL
```