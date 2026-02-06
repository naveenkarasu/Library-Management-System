-- Library Management System Database Schema
-- MySQL 5.7 Compatible

-- Create database
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS books;

-- Books table
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(20) UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    publisher VARCHAR(255),
    quantity INT DEFAULT 1,
    available INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Members table
CREATE TABLE members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    member_type ENUM('student', 'faculty') DEFAULT 'student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    member_id INT,
    issue_date DATE,
    due_date DATE,
    return_date DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0,
    status ENUM('issued', 'returned') DEFAULT 'issued',
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

-- Create indexes for better performance
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_members_name ON members(name);
CREATE INDEX idx_members_email ON members(email);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_due_date ON transactions(due_date);

-- Insert sample data for testing

-- Sample Books
INSERT INTO books (isbn, title, author, publisher, quantity, available) VALUES
('978-0-13-468599-1', 'The Pragmatic Programmer', 'David Thomas, Andrew Hunt', 'Addison-Wesley', 5, 5),
('978-0-596-00712-6', 'Head First Design Patterns', 'Eric Freeman, Elisabeth Robson', 'O\'Reilly Media', 3, 3),
('978-0-13-235088-4', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 4, 4),
('978-0-201-63361-0', 'Design Patterns', 'Gang of Four', 'Addison-Wesley', 2, 2),
('978-0-321-12521-7', 'Domain-Driven Design', 'Eric Evans', 'Addison-Wesley', 3, 3),
('978-0-13-468599-2', 'Introduction to Algorithms', 'Thomas H. Cormen', 'MIT Press', 6, 6),
('978-0-596-51774-8', 'JavaScript: The Good Parts', 'Douglas Crockford', 'O\'Reilly Media', 4, 4),
('978-0-321-35668-0', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 5, 5);

-- Sample Members
INSERT INTO members (name, email, phone, member_type) VALUES
('John Smith', 'john.smith@university.edu', '555-0101', 'student'),
('Jane Doe', 'jane.doe@university.edu', '555-0102', 'student'),
('Dr. Robert Brown', 'r.brown@university.edu', '555-0103', 'faculty'),
('Alice Johnson', 'alice.j@university.edu', '555-0104', 'student'),
('Prof. Michael Davis', 'm.davis@university.edu', '555-0105', 'faculty'),
('Emily Wilson', 'emily.w@university.edu', '555-0106', 'student');

-- Sample Transactions (some active, some returned)
INSERT INTO transactions (book_id, member_id, issue_date, due_date, return_date, fine_amount, status) VALUES
(1, 1, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 3 DAY), NULL, 0, 'issued'),
(2, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 9 DAY), NULL, 0, 'issued'),
(3, 3, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 6 DAY), DATE_SUB(CURDATE(), INTERVAL 4 DAY), 1.00, 'returned'),
(4, 1, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 1 DAY), NULL, 0, 'issued');

-- Update available count for issued books
UPDATE books SET available = available - 1 WHERE book_id IN (1, 2, 4);
