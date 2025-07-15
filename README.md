FinTrack – Expense Tracker REST API (Spring Boot + MySQL)

This repository contains the backend logic of a personal expense tracking application. The system allows users to manage their income, expenses, and spending categories with clean APIs built using Spring Boot and Java.

🔍 Project Overview
The purpose of this project is to help users manage their personal finances. Users can sign up, log in, add expenses with categories, track recurring costs, and update or delete past transactions. The project simulates a real-world finance management backend with proper layer separation and exception handling.

✅ What This Project Covers
- 🔐 User Signup and Signin with encrypted passwords
- ➕ Create a new expense with category, amount, and recurrence
- 📥 Get all expenses by user
- ✏️ Update existing expenses
- ❌ Soft delete for users and expenses
- 📄 Swagger UI documentation for API testing

⚙️ Tech Stack Used
- Language: Java 17
- Framework: Spring Boot
- Database: MySQL
- ORM: Spring Data JPA (Hibernate)
- API Docs: SpringDoc OpenAPI (Swagger UI)
- Build Tool: Maven
- Project Structure: Follows Controller → Service → Repository → Model + DTO

🚀 How to Run

✅ Prerequisites
- MySQL installed and running
- Database `fintrack` created manually
- IntelliJ IDEA (Community Edition works)
- JDK 17+
- Maven installed

✅ Steps to Run
1. Clone this repository.
2. Create a MySQL database named `fintrack`.
3. Update `application.properties` with your DB username and password.
4. Build and run the project using IntelliJ or the terminal:
```bash
./mvnw spring-boot:run

Open http://localhost:8080/swagger-ui/index.html to access the Swagger UI and test the APIs.

🧪 Sample Endpoints
- POST /api/users/signup – Register a new user
- POST /api/users/signin – Authenticate user
- POST /api/expenses – Add new expense
- GET /api/expenses/{userId} – Fetch all expenses for a user
- PUT /api/expenses/{expenseId} – Update existing expense
- DELETE /api/expenses/{expenseId} – Soft delete an expense

📌 Features Implemented
- Password hashing with BCrypt
- Category-based expense grouping
- DTOs used for clean API structure
- Centralized exception handling

🛠️ Future Improvements
- Add JWT Authentication for secure access
- Add analytics per month/category
- Export expenses to CSV
- Budget limit alerts for monthly spending

✍️ Author
Shadab Choudhary
Java | Spring Boot | Backend Developer
