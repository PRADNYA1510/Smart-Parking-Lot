# Smart Parking Lot Management System

## Overview

The Smart Parking Lot Management System is a backend application developed using Java, Spring Boot, Spring Data JPA, and PostgreSQL. It manages parking floors, parking spots, vehicle registration, parking sessions, tickets, and parking fee calculation.

## Features

- **Parking Floor Management:** Create and retrieve parking floors.
- **Parking Spot Management:** Manage parking spots and check availability.
- **Vehicle Management:** Register vehicles and track vehicle details.
- **Parking Entry and Exit:** Manage parking sessions and ticket lifecycle.
- **Fee Calculation:** Calculate parking fees based on vehicle type and parking duration.
- **Ticket Management:** Retrieve parking tickets and vehicle parking history.
- **Statistics and Revenue:** Retrieve parking statistics and revenue information.
- **Data Persistence:** Store application data in PostgreSQL.
- **Validation:** Validate parking operations and prevent duplicate floor numbers.

## Technology Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JUnit

## Prerequisites

- JDK compatible with the project configuration
- Maven
- PostgreSQL
- An IDE such as IntelliJ IDEA or VS Code

## Configuration

Configure the following environment variables before starting the application:

- `DB_URL` — PostgreSQL JDBC connection URL
- `DB_USERNAME` — PostgreSQL username
- `DB_PASSWORD` — PostgreSQL password

Example database URL:

`jdbc:postgresql://localhost:5432/smart_parking`

Do not commit actual database passwords or other secrets to version control.

## Running the Application

1. Create the PostgreSQL database.
2. Configure the required environment variables.
3. Open a terminal in the project root.
4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## Running Tests

Run the test suite using:

```bash
mvn test
```

## Project Structure

The application separates its responsibilities into components such as:

- **Controller:** Handles HTTP requests and responses.
- **Service:** Implements application business logic.
- **Repository:** Handles database operations.
- **Entity:** Represents persisted domain data.
- **DTO:** Defines data transferred between API layers.

## Future Improvements

- Add authentication and authorization.
- Add more comprehensive validation and exception handling.
- Improve test coverage for edge cases.
- Add API documentation using OpenAPI/Swagger.