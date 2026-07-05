# Enviro365 Junior Developer Assessment

Full-stack investor portfolio and withdrawal notice application built for the Enviro365 junior developer assessment.

## Stack

- Backend: Spring Boot 3, Java 21, Maven, H2, Spring Data JPA, Bean Validation, OpenAPI/Swagger
- Frontend: React 18, TypeScript, Vite, Material UI
- Advanced requirements included: global exception handling, DTO layer, input validation, unit tests, UI validation

## Project Structure

```text
enviro365-assessment/
  backend/
  frontend/
  screenshots/
  docker-compose.yml
  README.md
```

## Run Locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

The backend starts on `http://localhost:8080`.

Useful links:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:enviro365`
- Username: `sa`
- Password: blank

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173`.

## Seed Data

The app starts with two investors:

- Investor `1`: Grace Mokoena, older than 65, has retirement and tax-free products
- Investor `2`: Thabo Dlamini, younger than 65, has retirement and savings products

## API Documentation

### Retrieve Portfolio

```http
GET /api/investors/{investorId}/portfolio
```

Returns investor details, age, and all linked products with balances.

### Create Withdrawal Notice

```http
POST /api/withdrawals
Content-Type: application/json

{
  "investorId": 1,
  "productId": 1,
  "amount": 1000.00
}
```

Rules enforced:

- Retirement product withdrawals are allowed only when the investor is older than 65
- Withdrawal cannot exceed the current balance
- Withdrawal cannot exceed 90% of the current balance

### Withdrawal History

```http
GET /api/withdrawals?investorId=1
```

### CSV Statement Export

```http
GET /api/withdrawals/statement.csv?investorId=1&from=2026-01-01&to=2026-12-31
```

`from` and `to` are optional ISO date filters.

## Tests

```bash
cd backend
mvn test
```

The unit/integration tests cover successful withdrawal creation and key business rule failures.

## Docker Compose

```bash
docker compose up --build
```

This starts the backend and frontend services together.

## Screenshots

Place final screenshots in the `screenshots/` folder before submission. Suggested screenshots:

- Portfolio dashboard
- Withdrawal validation error
- Successful withdrawal notice
- Withdrawal history table
- CSV download/filter controls

## AI Usage Disclosure

AI assistance was used to scaffold and refine the project structure, backend services, validation rules, frontend components, and documentation. The implementation should still be reviewed, run, and tested by the submitter before delivery.
