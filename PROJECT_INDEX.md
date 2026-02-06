# Project Index: Library Management System

Generated: 2026-02-06

## Project Structure

```
library-management-system/
├── pom.xml                          # Legacy WAR project (kept for reference)
├── docker-compose.yml               # MySQL (3307) + Backend (8081) + Frontend (3001)
├── README.md
├── backend/
│   ├── pom.xml                      # Spring Boot 3.2.5 (Java 17)
│   ├── Dockerfile                   # temurin:17-jdk -> temurin:17-jre
│   ├── src/main/java/com/library/
│   │   ├── LibraryApplication.java  # Entry point (port 8081)
│   │   ├── config/
│   │   │   ├── SecurityConfig.java  # JWT filter chain + BCrypt + CORS
│   │   │   ├── JwtAuthFilter.java   # OncePerRequestFilter for JWT
│   │   │   ├── JwtUtil.java         # Token generation/validation (JJWT 0.12.5)
│   │   │   └── OpenApiConfig.java   # Swagger/OpenAPI 3.0
│   │   ├── controller/
│   │   │   ├── AuthController.java      # /api/v1/auth/**
│   │   │   ├── BookController.java      # /api/v1/books/**
│   │   │   ├── MemberController.java    # /api/v1/members/**
│   │   │   ├── TransactionController.java # /api/v1/transactions/**
│   │   │   └── ReportController.java    # /api/v1/reports/**
│   │   ├── model/
│   │   │   ├── Book.java            # isbn, title, author, publisher, quantity, available
│   │   │   ├── Member.java          # name, email, phone, memberType (STUDENT/FACULTY)
│   │   │   ├── Transaction.java     # book, member, issueDate, dueDate, returnDate, fine
│   │   │   ├── User.java            # username, password, role
│   │   │   ├── MemberType.java      # STUDENT (5 books/14 days), FACULTY (10 books/30 days)
│   │   │   └── TransactionStatus.java # ISSUED, RETURNED
│   │   ├── repository/              # Spring Data JPA with custom queries
│   │   ├── service/
│   │   │   ├── AuthService.java     # Login + JWT, register, findByUsername
│   │   │   ├── BookService.java     # CRUD, search, availability check
│   │   │   ├── MemberService.java   # CRUD, search, type validation
│   │   │   ├── TransactionService.java # Issue/return, fine calculation, loan limits
│   │   │   └── ReportService.java   # Dashboard stats, overdue reports
│   │   └── dto/                     # BookDto, MemberDto, IssueBookRequest, etc.
│   ├── src/main/resources/
│   │   ├── application.properties   # Port 8081, MySQL library_db, JWT config
│   │   └── db/migration/
│   │       ├── V1__initial_schema.sql  # books, members, transactions, users
│   │       └── V2__seed_data.sql       # 8 books, 6 members, admin user
│   └── src/test/java/com/library/
│       ├── service/BookServiceTest.java
│       └── service/TransactionServiceTest.java
├── frontend/
│   ├── package.json                 # React 18.3.1, Vite 6.0.1
│   ├── vite.config.ts              # base: '/Library-Management-System/'
│   ├── tailwind.config.js          # Teal + gold palette + shadcn variables
│   ├── Dockerfile                   # Node 20 + Nginx
│   ├── nginx.conf                   # Proxy /api/ -> backend:8081
│   ├── public/mockServiceWorker.js  # MSW service worker
│   └── src/
│       ├── main.tsx                 # React + QueryClient + MSW demo mode
│       ├── App.tsx                  # Routes + Layout
│       ├── pages/
│       │   ├── LoginPage.tsx        # FloatingBooks 3D background
│       │   ├── Dashboard.tsx        # Stats cards + BookShelf3D + StatsRing3D
│       │   ├── BooksPage.tsx        # Book listing + search + CRUD
│       │   ├── MembersPage.tsx      # Member management
│       │   ├── IssueBookPage.tsx    # Issue books with validation
│       │   ├── ReturnBookPage.tsx   # Return books + fine display
│       │   └── ReportsPage.tsx      # Overdue analytics
│       ├── components/
│       │   ├── layout/              # Layout, Header, Sidebar
│       │   ├── books/               # BookTable, BookForm, BookSearch
│       │   ├── members/             # MemberTable, MemberForm
│       │   ├── three/               # BookShelf3D, StatsRing3D, FloatingBooks
│       │   └── ui/                  # shadcn: button, input, card, dialog, toast, etc.
│       ├── hooks/
│       │   ├── useAuth.ts           # Zustand store (persist to localStorage)
│       │   ├── useBooks.ts          # TanStack Query CRUD hooks
│       │   ├── useMembers.ts
│       │   ├── useTransactions.ts
│       │   └── useReports.ts        # useDashboardStats()
│       ├── api/
│       │   ├── client.ts            # Axios + JWT interceptor
│       │   ├── auth.ts, books.ts, members.ts, transactions.ts, reports.ts
│       ├── mocks/
│       │   ├── browser.ts           # MSW setup with serviceWorker URL
│       │   ├── handlers.ts          # All API route handlers (~200 lines)
│       │   └── data.ts              # 8 books, 6 members, 8 transactions
│       ├── types/index.ts
│       └── lib/utils.ts             # clsx + tailwind-merge
└── .github/workflows/
    └── deploy-frontend.yml          # GitHub Pages with VITE_DEMO_MODE=true
```

## Entry Points

- **Backend**: `backend/src/main/java/com/library/LibraryApplication.java` (port 8081)
- **Frontend**: `frontend/src/main.tsx` - React app with MSW demo mode
- **Docker**: `docker-compose.yml` - MySQL (3307) + Backend (8081) + Frontend (3001)

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /api/v1/auth/login | Login + get JWT |
| POST | /api/v1/auth/register | Register user |
| GET | /api/v1/auth/me | Current user info |
| GET | /api/v1/books | List all books |
| POST | /api/v1/books | Create book |
| GET | /api/v1/books/{id} | Get book |
| PUT | /api/v1/books/{id} | Update book |
| DELETE | /api/v1/books/{id} | Delete book |
| GET | /api/v1/books/search?q= | Search books |
| GET | /api/v1/members | List members |
| POST | /api/v1/members | Create member |
| GET | /api/v1/members/{id} | Get member |
| PUT | /api/v1/members/{id} | Update member |
| DELETE | /api/v1/members/{id} | Delete member |
| GET | /api/v1/transactions | All transactions |
| GET | /api/v1/transactions/active | Active only |
| GET | /api/v1/transactions/overdue | Overdue only |
| POST | /api/v1/transactions/issue | Issue book |
| POST | /api/v1/transactions/{id}/return | Return book |
| GET | /api/v1/reports/dashboard | Dashboard stats |
| GET | /api/v1/reports/overdue | Overdue report |

## Key Dependencies

### Backend
| Dependency | Version | Purpose |
|-----------|---------|---------|
| spring-boot-starter-web | 3.2.5 | REST API |
| spring-boot-starter-data-jpa | 3.2.5 | ORM |
| spring-boot-starter-security | 3.2.5 | Auth |
| springdoc-openapi-starter-webmvc-ui | 2.3.0 | Swagger |
| flyway-core + flyway-mysql | auto | Migrations |
| jjwt-api/impl/jackson | 0.12.5 | JWT |

### Frontend
| Package | Version | Purpose |
|---------|---------|---------|
| react | 18.3.1 | UI framework |
| @tanstack/react-query | 5.62.2 | Server state |
| zustand | 5.0.2 | Client state |
| three | 0.170.0 | 3D graphics |
| @react-three/fiber + drei | 8.x/9.x | React Three.js |
| tailwindcss | 3.4.16 | CSS |
| msw | 2.6.8 | Mock API |
| vite | 6.0.1 | Build tool |

## Business Logic

- **Fine calculation**: $0.50/day overdue
- **Loan limits**: STUDENT: 5 books / 14 days, FACULTY: 10 books / 30 days
- **ISBN uniqueness** enforced on book creation

## Build Commands

```bash
# Backend
cd backend && JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean package

# Frontend
cd frontend && npm ci && npm run build

# Docker
docker-compose up -d
```
