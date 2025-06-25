# BankingApplication

# 💰 Vert.x Banking API (Basic)

A simple reactive banking service built with [Eclipse Vert.x](https://vertx.io/), demonstrating core Vert.x concepts like event loops, non-blocking HTTP routing, JSON handling, and basic in-memory data management.

---

## 🚀 Features

- Create a new bank account
- View account balance
- Deposit money into account
- Withdraw money from account (with balance check)

---

## 🛠 Tech Stack

- Java 17+
- Vert.x Core
- Vert.x Web
- JSON support
- Gradle

---

## 📦 Project Structure

```
vertx-banking-api/
├── src/
│ └── main/
│ ├── java/
│ │ └── com/example/
│ │   ├── banking/
│ │   │ ├── controller/
│ │   │ │ └── AccountController.java # HTTP route handlers
│ │   │ ├── exception/
│ │   │ │ ├── AccountNotFoundException.java
│ │   │ │ └── InsufficientBalanceException.java
│ │   │ ├── router/
│ │   │ │ └── AccountRouter.java
│ │   │ ├── service/
│ │   │ │ └── AccountService.java # Business logic (in-memory store)
│ │   │ ├── util/
│ │   │ │ ├── AccountHelper.java
│ │   │ │ ├── ResponseUtil.java
│ │   │ │ └── ValidationUtil.java
│ │   │ ├── verticles/
│ │   │ │ └── MainVerticle.java # App entry point
│ │   └── Launcher.java # Verticle launcher
│ └── resources/
└── build.gradle
```

---

## 📘 API Endpoints

### ➕ Create Account
`POST /api/account/create`  
**Request Body:**
```json
{
  "name": "John Doe",
  "initialBalance": 1000
}
```

**Response:**
```json
{
  "id": "12345",
  "name": "John Doe",
  "balance": 1000
}
```

### 💰 View Balance
`GET /api/account/{id}/balance`
**Response:**
```json
{
  "id": "12345",
  "balance": 1000
}
```

### ➕ Deposit Money
`POST /api/account/{id}/deposit`
**Request Body:**
```json
{
  "amount": 500
}
```
**Response:**
```json
{
  "id": "12345",
  "balance": 1500
}
```

### ➖ Withdraw Money
`POST /api/account/{id}/withdraw`
**Request Body:**
```json
{
  "amount": 200
}
```
**Response:**
```json
{
  "id": "12345",
  "balance": 1300
}
```

### ❗ Error Handling
If an operation fails (e.g., insufficient funds), the API will return a 400 Bad Request with an error message:
```json
{
  "error": "Insufficient funds"
}
```
---

## 🏗 Build & Run
### Using Gradle
1. Ensure you have [Java 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) and [Gradle](https://gradle.org/install/) installed.
2. Run ./gradlew run
3. The server will start on `http://localhost:8888`.

## 📬 Postman Collection
A ready-to-use Postman collection is available at `src/test/resources/postman` to test the APIs easily.
