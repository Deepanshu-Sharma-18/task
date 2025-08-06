
# 📝 Task Tracker - Spring Boot Web Application

This is a **Task Management Web Application** built with **Spring Boot**. It allows users to manage tasks (create, update, delete, view), and is equipped with JWT-based authentication, Docker support, unit/integration testing, and Swagger for API documentation.

---

## 🚀 Features

- ✅ User Registration & Login (JWT based)
- ✅ CRUD operations for Tasks
- ✅ One-to-Many Relationship (User → Tasks)
- ✅ API request validation and exception handling
- ✅ Logging with SLF4J + Logback
- ✅ Swagger UI for API testing
- ✅ Environment-based configuration using `.env`
- ✅ Docker & Docker Compose setup
- ✅ Unit testing with JUnit & Mockito

---

## 🛠️ Tech Stack

| Layer           | Tech Used                        |
|----------------|----------------------------------|
| Backend         | Spring Boot                      |
| Authentication  | JWT (JSON Web Tokens)            |
| Testing         | JUnit 5, Mockito                 |
| Documentation   | Swagger / OpenAPI                |
| Database        | MySQL (via Docker container)     |
| ORM             | Spring Data JPA                  |
| Logging         | SLF4J + Logback                  |
| Configuration   | `.env` + `application.properties`|
| Containerization| Docker, Docker Compose           |

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/task
│   │   ├── controller/         # REST APIs
│   │   ├── dto/                # Request/Response DTOs
│   │   ├── entity/             # JPA Entities
│   │   ├── repository/         # Spring Data Repositories
│   │   ├── security/           # JWT configs and filters
│   │   ├── service/            # Business Logic
│   │   ├── config/             # Swagger, CORS, etc.
│   │   └── TaskApplication.java# Main class
│   └── resources/
│       ├── application.properties
│       ├── logback-spring.xml (optional)
│       └── static/templates...
└── test/
    └── ... (JUnit + Mockito tests)
```

---

## 🔐 Authentication - JWT

- Users authenticate using JWT (stateless)
- JWT filters are applied using `OncePerRequestFilter`
- Secure endpoints using `@PreAuthorize` or path matchers
- Token contains User ID and expiry information

---

## 📦 Environment Configuration

Environment variables are managed through a `.env` file.

**Sample `.env`**
```env
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/task_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=admin
JWT_SECRET=your_jwt_secret_key
```

Update `application.properties` to read from environment:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

jwt.secret=${JWT_SECRET}
```

---

## 🧪 Testing with JUnit + Mockito

- All services and controllers are unit tested
- Mock dependencies with `@Mock` and inject with `@InjectMocks`
- API endpoint testing using `@WebMvcTest`

---

## 📦 Docker Setup

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/task-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### docker-compose.yml

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8
    container_name: mysql
    environment:
      MYSQL_DATABASE: task_db
      MYSQL_ROOT_PASSWORD: admin
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  app:
    build: .
    container_name: task-app
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    env_file:
      - .env

volumes:
  mysql_data:
```

### Build and Run

```bash
docker-compose up --build
```

---

## 📖 Swagger API Docs

Swagger UI is enabled for testing and exploring APIs.

- URL: `http://localhost:8080/swagger-ui.html` or `/swagger-ui/index.html`

Enable with:

```java
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    // Bean config
}
```

---

## 📌 Sample Endpoints

| Method | Endpoint            | Description          |
|--------|---------------------|----------------------|
| POST   | /api/auth/register  | Register new user    |
| POST   | /api/auth/login     | Login with JWT token |
| GET    | /api/tasks          | Get all user tasks   |
| POST   | /api/tasks          | Create a new task    |
| PUT    | /api/tasks/{id}     | Update a task        |
| DELETE | /api/tasks/{id}     | Delete a task        |

---

## 👥 Future Enhancements

- Role-based access (admin/user)
- Task prioritization and tags
- Pagination & filtering
- Frontend with React or Angular
- Email notifications

---

## 🧑‍💻 Author

**Deepanshu Sharma**  
📧 [Email] | 🌐 [LinkedIn] | 💼 [GitHub]  
> *Feel free to connect or contribute!*

---

## 📝 License

This project is open-source and free to use under the [MIT License](LICENSE).
