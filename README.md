# Impact Analysis - Spring Boot Backend (`impact-analysis-springboot`)

Backend customer service REST API built with **Java 21**, **Spring Boot 3.4**, **Spring Data JPA**, **Jakarta Validation**, **Flyway**, and **PostgreSQL**. Designed for an **AI-powered cross-repository PR Impact Analysis POC**, maintaining clear contracts with the companion React frontend repository (`impact-analysis-react`).

---

## Architectural Overview

```
                      Client / React Application (:5173)
                                      │
                                      ▼
                             CustomerController
                           (POST /api/customers)
                                      │
                   ┌──────────────────┴──────────────────┐
                   ▼                                     ▼
        Jakarta Bean Validation                 CustomerService
       (CustomerRequest constraints)       (Normalizes, Trims, Business Logic)
                                                         │
                                                         ▼
                                                 CustomerRepository
                                              (Spring Data JPA / SQL)
                                                         │
                                                         ▼
                                               PostgreSQL Database
                                            (Flyway Managed Migrations)
```

- **Controller (`com.example.impactanalysis.controller`):** Exposes REST APIs, handles HTTP status codes (201, 400, 409), and applies validation.
- **Service (`com.example.impactanalysis.service`):** Trims inputs, normalizes email addresses to lowercase, checks duplicate emails, and maps entities to DTOs.
- **Repository (`com.example.impactanalysis.repository`):** Handles persistence and queries against PostgreSQL.
- **DTOs (`com.example.impactanalysis.dto`):** Exact schema alignment with TypeScript interfaces in `impact-analysis-react`.
- **Database & Migrations (`src/main/resources/db/migration`):** Flyway SQL migrations for version-controlled database schema evolution.

---

## Project Structure

```
impact-analysis-springboot/
├── docker-compose.yml       # Local PostgreSQL 16 container definition
├── pom.xml                  # Maven dependencies & build plugins
├── README.md                # Comprehensive project documentation
├── .gitignore               # Ignored build artifacts and configs
└── src/
    ├── main/
    │   ├── java/com/example/impactanalysis/
    │   │   ├── ImpactAnalysisApplication.java
    │   │   ├── config/
    │   │   │   ├── OpenApiConfig.java       # Swagger / OpenAPI documentation
    │   │   │   └── WebCorsConfig.java       # CORS mapping for http://localhost:5173
    │   │   ├── controller/
    │   │   │   └── CustomerController.java  # REST API mappings
    │   │   ├── dto/
    │   │   │   ├── CustomerRequest.java     # Incoming payload contract
    │   │   │   ├── CustomerResponse.java    # Outgoing payload contract
    │   │   │   └── ErrorResponse.java       # Structured error representation
    │   │   ├── entity/
    │   │   │   └── CustomerEntity.java      # JPA entity for 'customers' table
    │   │   ├── exception/
    │   │   │   ├── CustomerAlreadyExistsException.java
    │   │   │   └── GlobalExceptionHandler.java
    │   │   ├── repository/
    │   │   │   └── CustomerRepository.java  # Data access repository
    │   │   └── service/
    │   │       ├── CustomerService.java     # Service interface
    │   │       └── impl/CustomerServiceImpl.java
    │   └── resources/
    │       ├── application.yml              # Primary configuration (PostgreSQL)
    │       └── db/migration/
    │           └── V1__create_customers_table.sql # Flyway migration
    └── test/
        ├── java/com/example/impactanalysis/
        │   ├── CustomerIntegrationTest.java # Full SpringBootTest integration suite
        │   ├── controller/
        │   │   └── CustomerControllerTest.java # WebMvcTest for REST validation
        │   └── service/
        │       └── CustomerServiceTest.java    # Service unit tests
        └── resources/
            └── application-test.yml         # In-memory H2 profile for testing
```

---

## Prerequisites

- **Java Development Kit (JDK)**: Version 21 (LTS)
- **Apache Maven**: Version 3.8+ (Maven 3.9 included in environment)
- **Docker / PostgreSQL**: Optional for local containerized database (`docker-compose up -d`)

---

## PostgreSQL Database Configuration

### Option A: Using Docker Compose
Run the provided Docker Compose file to start PostgreSQL on port 5432:
```bash
docker compose up -d
```

### Option B: Local or Remote PostgreSQL Server
Configure the database via environment variables:
| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | JDBC Connection URL | `jdbc:postgresql://localhost:5432/customer_db` |
| `SPRING_DATASOURCE_USERNAME` | Database User | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database Password | `postgres` |

---

## How to Run Locally

### 1. Run Tests & Build
```bash
mvn clean package
```

### 2. Start Application
```bash
mvn spring-boot:run
```

The server starts on port `8080`.

### 3. Interactive OpenAPI / Swagger UI
Navigate to:
```
http://localhost:8080/swagger-ui.html
```
Raw OpenAPI documentation is available at:
```
http://localhost:8080/v3/api-docs
```

---

## API Reference

### 1. Register Customer
- **Endpoint:** `POST /api/customers`
- **Content-Type:** `application/json`

**Sample Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "9876543210",
  "message": "Sample customer registration"
}
```

**Success Response (201 Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "9876543210",
  "message": "Sample customer registration",
  "createdAt": "2026-11-16T17:35:00",
  "status": "SUCCESS"
}
```

**Validation Error Response (400 Bad Request):**
```json
{
  "timestamp": "2026-11-16T17:35:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for one or more fields",
  "path": "/api/customers",
  "fieldErrors": {
    "email": "Email Address must be a valid email format",
    "phone": "Phone Number must be a valid 10-digit number"
  }
}
```

---

## Cross-Repository AI Impact Analysis Compatibility

- `CustomerRequest.java` ⇄ `impact-analysis-react/src/types/customer.ts` (`CustomerRequest`)
- `CustomerResponse.java` ⇄ `impact-analysis-react/src/types/customer.ts` (`CustomerResponse`)
- `@RequestMapping("/api/customers")` ⇄ `impact-analysis-react/src/services/customerService.ts` (`CUSTOMERS_ENDPOINT`)
- Error response schema ⇄ `impact-analysis-react/src/types/customer.ts` (`ApiErrorResponse`)
