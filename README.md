# Library Management System

A web-based application for managing university library operations including book cataloging, member registration, book issuing/returning with automated due date tracking and fine calculation. Modernized from Java Servlets/JSP to a Spring Boot REST API with a React/TypeScript frontend featuring an interactive 3D bookshelf visualization.

**[Live Demo](https://naveenkarasu.github.io/Library-Management-System/)** (runs with mock data, no backend required)

## Tech Stack

### Backend
- **Java 17**, Spring Boot 3.2.5, Spring Data JPA, Spring Security 6
- **JWT Authentication** for stateless API
- **REST API** with versioned endpoints (`/api/v1/`)
- **Swagger/OpenAPI** documentation (`/swagger-ui.html`)
- **Database:** MySQL 8.0 with Flyway migrations
- **Testing:** JUnit 5, Mockito
- **Build Tool:** Apache Maven

### Frontend
- **React 18** with TypeScript 5
- **Vite 5** for fast builds and HMR
- **Tailwind CSS** + shadcn/ui components
- **Three.js** (React Three Fiber + Drei) - 3D interactive bookshelf
- **TanStack Query v5** for server state management
- **Zustand** for auth state
- **MSW** (Mock Service Worker) for demo mode
- **Axios** with JWT interceptors

## Quick Start with Docker

```bash
docker-compose up --build
```

This starts MySQL (port 3307), the Spring Boot backend (port 8081), and the React frontend (port 3001).

Open: `http://localhost:3001`

## Manual Setup

### Prerequisites

- JDK 17
- Node.js 20+
- Apache Maven 3.x
- MySQL Server 8.0+

### Backend

```bash
cd backend

# Database is auto-created by Flyway migrations
# Update credentials in src/main/resources/application.properties if needed

mvn spring-boot:run
```

Backend runs at `http://localhost:8081`
Swagger UI at `http://localhost:8081/swagger-ui.html`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173`

## Default Credentials

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | admin    | admin123  |

## Features

- **3D Interactive Bookshelf** - Three.js visualization with color-coded books by availability, hover tooltips, click-to-view details, and orbit controls
- **Book Management** - Add, edit, delete, and search books by title/author/ISBN
- **Member Management** - Register new members (students/faculty), update details
- **Issue Books** - Issue books to members with automatic due date calculation based on member type
- **Return Books** - Return books with automatic fine calculation for late returns
- **Reports** - Dashboard with library stats, overdue books report, inventory overview
- **JWT Authentication** - Secure stateless API with BCrypt password hashing
- **Demo Mode** - Full frontend experience with MSW mock data (for GitHub Pages)

## Fine Calculation

| Member Type | Max Books | Loan Period | Fine Rate     |
|-------------|-----------|-------------|---------------|
| Student     | 5         | 14 days     | $0.50 per day |
| Faculty     | 10        | 30 days     | $0.50 per day |

## REST API Endpoints

| Method | URL                              | Description             |
|--------|----------------------------------|-------------------------|
| POST   | /api/v1/auth/login               | Authenticate user       |
| GET    | /api/v1/books                    | List all books          |
| POST   | /api/v1/books                    | Add a new book          |
| GET    | /api/v1/books/{id}               | Get book details        |
| PUT    | /api/v1/books/{id}               | Update a book           |
| DELETE | /api/v1/books/{id}               | Delete a book           |
| GET    | /api/v1/books/search?q=          | Search books            |
| GET    | /api/v1/members                  | List all members        |
| POST   | /api/v1/members                  | Add a new member        |
| GET    | /api/v1/members/{id}             | Get member details      |
| PUT    | /api/v1/members/{id}             | Update a member         |
| DELETE | /api/v1/members/{id}             | Delete a member         |
| POST   | /api/v1/transactions/issue       | Issue a book            |
| POST   | /api/v1/transactions/return/{id} | Return a book           |
| GET    | /api/v1/transactions/active      | List active transactions|
| GET    | /api/v1/reports/overdue          | Overdue books report    |
| GET    | /api/v1/reports/stats            | Library statistics      |

Full API docs available at `/swagger-ui.html` when the backend is running.

## Project Structure

```
library-management-system/
├── docker-compose.yml
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/library/
│       │   ├── config/          # Security, JWT, OpenAPI config
│       │   ├── controller/      # REST API controllers
│       │   ├── model/           # JPA entities + enums
│       │   ├── repository/      # Spring Data repositories
│       │   └── service/         # Business logic (fine calc, reports)
│       └── resources/
│           ├── application.properties
│           └── db/migration/    # Flyway SQL migrations
├── frontend/
│   ├── package.json
│   ├── vite.config.ts
│   ├── Dockerfile               # Nginx-based production container
│   └── src/
│       ├── components/          # React components + 3D bookshelf
│       ├── pages/               # Route pages (Dashboard, Books, etc.)
│       ├── hooks/               # Custom hooks
│       ├── api/                 # API client layer
│       ├── mocks/               # MSW handlers + demo data
│       └── types/               # TypeScript types
└── legacy/                      # Original Servlets/JSP code (branch)
```

## License

This project was made for educational purposes. Feel free to use or modify it.
