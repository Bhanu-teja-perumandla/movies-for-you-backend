# Movies For You : backend

A Spring Boot-based RESTful API backend for the Movies For You application.

## 🎬 Overview

This backend application serves as the API for the Movies For You, providing endpoints for:
- User authentication and authorization
- Movie data retrieval and management
- User favorites functionality

[//]: # (- Movie review system)

[//]: # (- Admin-specific operations)

## 🚀 Technologies

- **Java 21** - Programming language
- **Spring Boot** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access layer
- **PostgreSQL** - Relational database
- **JUnit & Mockito** - Testing frameworks
- **Gradle** - Dependency management
- **Lombok** - Boilerplate code reduction

[//]: # (- **Swagger/OpenAPI** - API documentation)

## 📋 Prerequisites

- JDK 21+
- SpringBoot 3.4+
- PostgreSQL 14+

## 🔧 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Bhanu-teja-perumandla/movies-for-you-backend.git
   cd movies-for-you-backend
   ```

2. Configure the database connection in `.env`:
   ```properties
     DB_URL='your-db-url'
     DB_USER='your-user'
     DB_PASSWORD='your-password'
     DEFAULT_PASSWORD='your-password'
     CLIENT_URL=http://localhost:3000
   ```

3. Build the project:
   ```bash
   ./gradlew clean build
   ```

## 🏃‍♂️ Running the Application

Start the application:
```bash
./gradlew bootRun
```

The API will be available at `http://localhost:8080/`.

> **Note:** The server runs on port 8080 by default. Make sure this port is available on your system. If you need to use a different port, you can configure it in `application.properties` with `server.port=YOUR_PORT`.

## 🧪 Testing

Run tests with:
```bash
./gradlew test
```

[//]: # (## 🔄 Project Structure)

[//]: # ()
[//]: # (```)

[//]: # (src/)

[//]: # (├── main/)

[//]: # (│   ├── java/com/yourapp/movieexplorer/)

[//]: # (│   │   ├── config/          # Configuration classes)

[//]: # (│   │   ├── controller/      # REST controllers)

[//]: # (│   │   ├── dto/             # Data Transfer Objects)

[//]: # (│   │   ├── exception/       # Custom exceptions and handlers)

[//]: # (│   │   ├── model/           # Entity classes)

[//]: # (│   │   ├── repository/      # Data access interfaces)

[//]: # (│   │   ├── security/        # Security configurations)

[//]: # (│   │   ├── service/         # Business logic)

[//]: # (│   │   └── MovieExplorerApplication.java  # Main class)

[//]: # (│   └── resources/)

[//]: # (│       ├── application.properties  # Application configuration)

[//]: # (│       └── data.sql                # Initial data &#40;optional&#41;)

[//]: # (└── test/)

[//]: # (    └── java/com/yourapp/movieexplorer/)

[//]: # (        ├── controller/      # Controller tests)

[//]: # (        ├── repository/      # Repository tests)

[//]: # (        ├── service/         # Service tests)

[//]: # (        └── integration/     # Integration tests)

[//]: # (```)

## 📚 API Documentation

API documentation is available via Swagger UI at `http://localhost:8080/swagger-ui.html` when the application is running.

[//]: # (## 🔐 Authentication & Authorization)

[//]: # ()
[//]: # (The backend uses JWT &#40;JSON Web Token&#41; for authentication:)

[//]: # (- `/api/auth/register` - Register a new user)

[//]: # (- `/api/auth/login` - Authenticate and receive JWT)

[//]: # (- JWT must be included in the Authorization header for protected endpoints)

[//]: # (## 🎯 Key Endpoints)

[//]: # ()
[//]: # (### Authentication)

[//]: # (- `POST /api/auth/register` - Register new user)

[//]: # (- `POST /api/auth/login` - Authenticate user)

[//]: # ()
[//]: # (### Movies)

[//]: # (- `GET /api/movies` - Get all movies)

[//]: # (- `GET /api/movies/{id}` - Get movie by ID)

[//]: # (- `GET /api/movies/popular` - Get popular movies)

[//]: # ()
[//]: # (### Favorites)

[//]: # (- `GET /api/users/{userId}/favorites` - Get user favorites)

[//]: # (- `POST /api/users/{userId}/favorites/{movieId}` - Add favorite)

[//]: # (- `DELETE /api/users/{userId}/favorites/{movieId}` - Remove favorite)

[//]: # ()
[//]: # (### Reviews)

[//]: # (- `GET /api/movies/{movieId}/reviews` - Get reviews for a movie)

[//]: # (- `POST /api/movies/{movieId}/reviews` - Create a review)

[//]: # (- `PUT /api/reviews/{reviewId}` - Update a review)

[//]: # (- `DELETE /api/reviews/{reviewId}` - Delete a review)

[//]: # ()
[//]: # (### Admin Operations)

[//]: # (- `POST /api/admin/movies` - Add a new movie)

[//]: # (- `PUT /api/admin/movies/{id}` - Update movie)

[//]: # (- `DELETE /api/admin/movies/{id}` - Delete movie)

[//]: # (- `GET /api/admin/users` - Get all users)

## 🔜 Upcoming Features

- User-Admin Flow
- Review management

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request