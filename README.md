# Task Tracker

A Spring Boot full-stack task management app. Users register/sign in, create tasks
with status and due dates, and see a dashboard summarising their workload.

## 🚀 Live demo

**https://task-tracker-i7a0.onrender.com**

Register a new account (any username/password ≥ 4 chars) and sign in.
Hosted on Render's free tier — first request after 15 min of idle takes
~30 s to wake the dyno; subsequent requests are instant. The H2 in-memory
database is recreated on each restart, so demo data is ephemeral.

[![Deploy your own to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/Mohan143-web/task-tracker)

## Tech stack
- Spring Boot 2.7.18 (Java 16)
- Spring Security (form login, BCrypt)
- Spring Data JPA + H2 (in-memory)
- Thymeleaf + Bootstrap 5
- REST API at `/api/tasks`

## Features
- User registration & login (BCrypt-hashed passwords)
- Task CRUD with title, description, status (TODO / IN_PROGRESS / DONE), due date
- Per-user task isolation — owner-scoped queries
- Dashboard with status counts and overdue-task highlighting
- Thymeleaf views and a JSON REST API exposing the same data
- JPA `@OneToMany` / `@ManyToOne` relationships, `@Transactional` services, validation

## Run

```bash
mvn spring-boot:run
```

Open http://localhost:8080 — you'll be redirected to `/login`. Click *Create an account*.

H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:tasktracker`).

## REST API

```bash
# After logging in via /login (session cookie):
curl -b cookies.txt http://localhost:8080/api/tasks
curl -b cookies.txt -X POST http://localhost:8080/api/tasks \
     -H 'Content-Type: application/json' \
     -d '{"title":"Buy milk","status":"TODO","dueDate":"2026-05-10"}'
```

## Build

```bash
mvn clean package
java -jar target/task-tracker-0.0.1-SNAPSHOT.jar
```

## Project layout
```
src/main/java/com/mohan/tasktracker
├── TaskTrackerApplication.java
├── config/SecurityConfig.java
├── controller/{AuthController, TaskWebController, TaskRestController}.java
├── model/{User, Task}.java
├── repository/{UserRepository, TaskRepository}.java
└── service/{UserService, TaskService}.java
```
