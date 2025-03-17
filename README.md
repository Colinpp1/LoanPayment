# Loan Payment System

A Spring Boot application that manages loan payments and calculations.

## Features

- Create new loans with specified amount and term
- Make payments towards existing loans
- Track remaining balance and payment history
- RESTful API endpoints for loan management

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/Colinpp1/loan-payment-system.git
cd loan-payment-system
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Create a New Loan
```http
POST /api/loans
Content-Type: application/json

{
    "loanAmount": 10000,
    "term": 12
}
```

### Make a Payment
```http
POST /api/payments
Content-Type: application/json

{
    "loanId": 1,
    "paymentAmount": 1000
}
```

### Get Loan Details
```http
GET /api/loans/{loanId}
```

## Database

The application uses H2 in-memory database. The console can be accessed at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: ` ` (empty password)

## Technologies Used

- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- Lombok
- Maven
- Java 17

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
