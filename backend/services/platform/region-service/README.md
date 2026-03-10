# Region Service

A Spring Boot microservice for managing geographical regions, areas, sub-areas, and branches.

## Overview

This service provides REST APIs for managing hierarchical geographical data:
- **Areas**: Top-level geographical regions
- **Sub-areas**: Subdivisions within areas  
- **Branches**: Specific locations within sub-areas

## Features

- Full CRUD operations for areas, sub-areas, and branches
- Hierarchical data retrieval with nested relationships
- RESTful API with proper HTTP status codes
- JPA entities with PostgreSQL database
- Flyway database migrations
- Docker support with multi-stage builds
- Health check endpoints
- Comprehensive error handling

## API Endpoints

### Health Checks
- `GET /api/region/health` - Basic health check
- `GET /api/region/actuator/health` - Actuator health check

### Areas
- `GET /api/region/areas` - List all areas
- `POST /api/region/areas` - Create new area
- `GET /api/region/areas/{id}` - Get area by ID
- `PUT /api/region/areas/{id}` - Update area
- `DELETE /api/region/areas/{id}` - Delete area
- `GET /api/region/areas/hierarchy` - Get areas with nested sub-areas and branches

### Sub-Areas
- `GET /api/region/sub-areas` - List all sub-areas
- `POST /api/region/sub-areas` - Create new sub-area
- `GET /api/region/sub-areas/{id}` - Get sub-area by ID
- `PUT /api/region/sub-areas/{id}` - Update sub-area
- `DELETE /api/region/sub-areas/{id}` - Delete sub-area

### Branches
- `GET /api/region/branches` - List all branches
- `POST /api/region/branches` - Create new branch
- `GET /api/region/branches/{id}` - Get branch by ID
- `PUT /api/region/branches/{id}` - Update branch
- `DELETE /api/region/branches/{id}` - Delete branch

## Data Model

```json
{
  "area": {
    "id": "uuid",
    "name": "string",
    "description": "string|null",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  },
  "subArea": {
    "id": "uuid",
    "name": "string", 
    "description": "string|null",
    "areaId": "uuid",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  },
  "branch": {
    "id": "uuid",
    "name": "string",
    "description": "string|null", 
    "areaId": "uuid",
    "subAreaId": "uuid",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_HOST` | `0.0.0.0` | Server bind address |
| `SERVER_PORT` | `8086` | Server port |
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/region_service_db` | PostgreSQL JDBC URL |
| `DATABASE_USERNAME` | `postgres` | Database username |
| `DATABASE_PASSWORD` | `postgres` | Database password |
| `SHOW_SQL` | `false` | Enable SQL logging |

### Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE region_service_db;
```

The application will automatically create tables using Hibernate and manage migrations with Flyway.

## Development

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Running Locally

1. Copy environment configuration:
```bash
cp .env.example .env
```

2. Update `.env` with your database settings

3. Run the application:
```bash
mvn spring-boot:run
```

The service will be available at `http://localhost:8086`

### Building

```bash
mvn clean package
```

### Running Tests

```bash
mvn test
```

## Docker

### Build Image

```bash
docker build -t region-service .
```

### Run Container

```bash
docker run -p 8086:8086 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/region_service_db \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=postgres \
  region-service
```

### Using Docker Compose

```yaml
version: '3.8'
services:
  region-service:
    build: .
    ports:
      - "8086:8086"
    environment:
      - DATABASE_URL=jdbc:postgresql://postgres:5432/region_service_db
      - DATABASE_USERNAME=postgres
      - DATABASE_PASSWORD=postgres
    depends_on:
      - postgres
  
  postgres:
    image: postgres:13
    environment:
      - POSTGRES_DB=region_service_db
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

## Error Handling

The service returns appropriate HTTP status codes:

- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `204 No Content` - Resource deleted successfully
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Server error

Error responses include a descriptive message:

```json
{
  "message": "Area not found with id: 12345678-1234-1234-1234-123456789012"
}
```

## Monitoring

### Health Endpoints

- `/api/region/health` - Simple health check
- `/actuator/health` - Spring Boot Actuator health
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

### Logging

Configure logging levels in `application.yml` or environment variables:

```yaml
logging:
  level:
    com.platform.regionservice: INFO
    org.springframework.web: INFO
    org.hibernate.SQL: WARN
```

## Migration from Rust Version

This Spring Boot version provides equivalent functionality to the original Rust Actix Web implementation:

- Same API endpoints and response formats
- Same database schema (auto-generated by Hibernate)
- Same port (8086) and configuration options
- Enhanced with Spring Boot features (actuator, validation, etc.)

The migration maintains backward compatibility while adding enterprise-grade features.
