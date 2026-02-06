# Library Management System

A web-based application for managing university library operations including book cataloging, member registration, book issuing/returning with automated due date tracking and fine calculation. Built using Java Servlets, JSP, and MySQL with a Bootstrap-based responsive interface.

## Tech Stack

- **Backend:** Java 8, Servlets, JSP, JDBC
- **Frontend:** HTML, CSS, Bootstrap 3, JavaScript
- **Database:** MySQL 5.7
- **Build Tool:** Apache Maven
- **Server:** Apache Tomcat 8

## Prerequisites

- JDK 1.8 (Java 8)
- Apache Maven 3.x
- MySQL Server 5.7+
- Apache Tomcat 8.x

## Database Setup

1. Open MySQL and create the database:

```sql
CREATE DATABASE library_db;
USE library_db;
```

2. Run the schema file:

```bash
mysql -u root -p library_db < sql/schema.sql
```

3. Load sample data (optional):

```bash
mysql -u root -p library_db < sql/seed-data.sql
```

4. Update database credentials in `src/main/resources/db.properties` if your MySQL username/password is different from default (`root` / `root`).

## Build and Deployment

1. Build the project:

```bash
cd library-management-system
mvn clean package
```

2. Copy the WAR file to Tomcat:

```bash
cp target/library-management-system.war /path/to/tomcat/webapps/
```

3. Start Tomcat and open in browser:

```
http://localhost:8080/library-management-system/
```

## Features

- **Book Management** - Add, edit, delete, and search books by title/author/ISBN
- **Member Management** - Register new members (students/faculty), update details
- **Issue Books** - Issue books to members with automatic due date calculation
- **Return Books** - Return books with automatic fine calculation for late returns
- **Reports** - View overdue books, library inventory status
- **Search** - Search books by title, author, or ISBN

## Project Structure

```
library-management-system/
├── pom.xml
├── sql/
│   ├── schema.sql
│   └── seed-data.sql
├── src/
│   ├── main/
│   │   ├── java/com/library/
│   │   │   ├── dao/           # Data Access Objects
│   │   │   ├── model/         # Java Beans
│   │   │   ├── servlet/       # Servlet Controllers
│   │   │   └── util/          # Utility classes
│   │   ├── resources/
│   │   │   └── db.properties
│   │   └── webapp/
│   │       ├── WEB-INF/web.xml
│   │       ├── css/style.css
│   │       └── *.jsp          # JSP Views
│   └── test/
│       └── java/com/library/dao/
└── README.md
```

## Fine Calculation

- Due date is 14 days from issue date
- Fine is Rs. 5 per day for late returns
- Faculty members get 30 days instead of 14

## Known Issues

- No user authentication (anyone can access the system)
- Pagination not implemented for large book lists
- No email notifications for overdue books

## License

This project was made for educational purposes.
