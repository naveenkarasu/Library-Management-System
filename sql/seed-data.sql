-- Seed Data for Library Management System
-- Run this after schema.sql

USE library_db;

-- Sample Books
INSERT INTO books (isbn, title, author, publisher, quantity, available) VALUES
('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 3, 2),
('978-0596009205', 'Head First Design Patterns', 'Eric Freeman', 'O''Reilly Media', 2, 2),
('978-0132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 4, 3),
('978-0201633610', 'Design Patterns', 'Gang of Four', 'Addison-Wesley', 2, 1),
('978-0131872486', 'Thinking in Java', 'Bruce Eckel', 'Prentice Hall', 3, 3),
('978-0596007126', 'Head First Java', 'Kathy Sierra', 'O''Reilly Media', 5, 4),
('978-0321125217', 'Domain-Driven Design', 'Eric Evans', 'Addison-Wesley', 2, 2),
('978-0137081073', 'The Clean Coder', 'Robert C. Martin', 'Prentice Hall', 2, 1),
('978-0062316097', 'Sapiens', 'Yuval Noah Harari', 'Harper', 3, 3),
('978-0451524935', '1984', 'George Orwell', 'Signet Classics', 4, 3),
('978-0061120084', 'To Kill a Mockingbird', 'Harper Lee', 'Harper Perennial', 3, 2),
('978-0743273565', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Scribner', 2, 2);

-- Sample Members
INSERT INTO members (name, email, phone, member_type) VALUES
('Rahul Sharma', 'rahul.sharma@university.edu', '9876543210', 'student'),
('Priya Patel', 'priya.patel@university.edu', '9876543211', 'student'),
('Amit Kumar', 'amit.kumar@university.edu', '9876543212', 'student'),
('Dr. Suresh Nair', 'suresh.nair@university.edu', '9876543213', 'faculty'),
('Dr. Meena Iyer', 'meena.iyer@university.edu', '9876543214', 'faculty'),
('Vikram Singh', 'vikram.singh@university.edu', '9876543215', 'student'),
('Anjali Desai', 'anjali.desai@university.edu', '9876543216', 'student');

-- Sample Transactions (some issued, some returned, some overdue)
INSERT INTO transactions (book_id, member_id, issue_date, due_date, return_date, fine_amount, status) VALUES
(1, 1, '2017-10-01', '2017-10-15', '2017-10-14', 0.00, 'returned'),
(3, 2, '2017-10-05', '2017-10-19', '2017-10-25', 30.00, 'returned'),
(4, 4, '2017-10-10', '2017-10-24', NULL, 0.00, 'issued'),
(6, 3, '2017-10-12', '2017-10-26', '2017-10-20', 0.00, 'returned'),
(8, 5, '2017-11-01', '2017-11-15', NULL, 0.00, 'issued'),
(10, 1, '2017-11-05', '2017-11-19', NULL, 0.00, 'issued'),
(11, 6, '2017-10-20', '2017-11-03', '2017-11-10', 35.00, 'returned'),
(1, 7, '2017-11-10', '2017-11-24', NULL, 0.00, 'issued');
