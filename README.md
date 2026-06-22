# Restaurant API

A Spring Boot REST API for managing restaurants, menu items, users, and orders. This application uses layered architecture, JWT authentication, validation, and centralized exception handling.

## What this project contains

- Spring Boot 4 REST API
- MySQL database integration via Spring Data JPA
- JWT authentication with Spring Security
- File upload for menu item images
- Request validation using Jakarta Bean Validation
- Centralized exception handling for API errors

## Prerequisites

- Java 21+
- Maven 3.6+
- MySQL 8.0+
- Postman or curl for API testing

## Setup

### 1. Configure database

The application uses `src/main/resources/application.properties` for datasource configuration. It currently defaults to environment variables:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://restaurant-api-db-ankitgangwar1082006-fde1.e.aivencloud.com:26464/foodcourt_db?ssl-mode=REQUIRED}
spring.datasource.username=${DB_USERNAME:avnadmin}
spring.datasource.password=${DB_PASSWORD:######}
```

For local development, replace these values or set environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Example local database creation:

```sql
CREATE DATABASE foodcourt_db;
```

### 2. Run the application

```powershell
./mvnw.cmd spring-boot:run
```

Or with installed Maven:

```powershell
mvn spring-boot:run
```

The app starts on `http://localhost:8080` by default.

## Project structure

```
src/main/java/foodcourt/in/restaurant/
├── controller/          # REST controllers
├── service/             # Business logic
├── repository/          # JPA repositories
├── entity/              # database models
├── dto/                 # request/response payloads
├── exception/           # global error handling
├── security/            # JWT and Spring Security
└── RestaurantApplication.java
```

## Configuration notes

`src/main/resources/application.properties` also includes:

```properties
server.port=${PORT:8080}
spring.application.name=restaurant
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
```

## API Endpoints

### Restaurants
- `POST /api/restaurants`
- `GET /api/restaurants/{id}`
- `PUT /api/restaurants/{id}`

### Menu Items
- `POST /api/menu_items`
- `POST /api/menu_items/{id}/image`
- `GET /api/menu_items/{id}`
- `PUT /api/menu_items/{id}`
- `DELETE /api/menu_items/{id}`
- `GET /api/menu_items/page?page=0&size=10`

### Users
- `POST /api/users/register`
- `POST /api/users/login`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Orders
- `POST /api/order/book`
- `GET /api/order/{id}`
- `PUT /api/order/{id}`
- `DELETE /api/order/{id}`

## Authentication

This app uses JWT authentication.
- Public endpoints: `/api/users/register`, `/api/users/login`
- All other endpoints require `Authorization: Bearer <token>`

## Example requests

### Register a user

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Aman","email":"aman@example.com","password":"secret1","phoneNumber":"9876543210","address":"Mumbai"}'
```

### Login

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"aman@example.com","password":"secret1"}'
```

### Create a restaurant

```bash
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"My Diner","location":"Mumbai","status":true}'
```

### Create a menu item

```bash
curl -X POST http://localhost:8080/api/menu_items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"Paneer Butter Masala","restaurantId":1,"price":250.0}'
```

### Upload a menu item image

```bash
curl -X POST http://localhost:8080/api/menu_items/1/image \
  -H "Authorization: Bearer <token>" \
  -F "image=@/path/to/image.jpg"
```

### Book an order

```bash
curl -X POST http://localhost:8080/api/order/book \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"userId":1,"restaurantId":1,"list":[{"menuItemId":1,"quantity":2}]}'
```

## Error handling

The API returns structured exception responses:
- `400 Bad Request` for validation failures
- `404 Not Found` for missing resources
- `409 Conflict` for data integrity problems
- `500 Internal Server Error` for unexpected errors

## Testing

Run tests:

```bash
./mvnw.cmd test
```

Or with Maven:

```bash
mvn test
```

## Project health

- Build: ✅ `./mvnw.cmd -q -DskipTests compile` passes
- Tests: ✅ `./mvnw.cmd -q test` completes successfully
- No critical endpoint or security bug was found during review

## Notes

- JWT protection is enabled for all non-auth endpoints.
- The menu item image endpoint expects `multipart/form-data`.
- Local development requires proper DB credentials in `application.properties` or environment variables.

## Recommended improvements

- Add Swagger/OpenAPI documentation
- Add more unit and integration tests
- Add role-based access control for admin routes
- Improve file upload/media storage support

---

Import `postman_collection.json` from the repository root into Postman to use the pre-built request collection.
