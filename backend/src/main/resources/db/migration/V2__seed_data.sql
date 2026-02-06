-- Sample books
INSERT INTO books (isbn, title, author, publisher, quantity, available) VALUES
('978-0-13-468599-1', 'The Pragmatic Programmer', 'David Thomas, Andrew Hunt', 'Addison-Wesley', 3, 3),
('978-0-201-63361-0', 'Design Patterns', 'Erich Gamma et al.', 'Addison-Wesley', 2, 2),
('978-0-596-00712-6', 'Head First Design Patterns', 'Eric Freeman, Elisabeth Robson', 'O''Reilly Media', 4, 4),
('978-0-13-235088-4', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 3, 3),
('978-0-321-12521-7', 'Domain-Driven Design', 'Eric Evans', 'Addison-Wesley', 2, 2),
('978-0-13-476904-3', 'Refactoring', 'Martin Fowler', 'Addison-Wesley', 2, 2),
('978-0-596-51774-8', 'JavaScript: The Good Parts', 'Douglas Crockford', 'O''Reilly Media', 5, 5),
('978-1-49-195016-0', 'Fluent Python', 'Luciano Ramalho', 'O''Reilly Media', 3, 3);

-- Sample members
INSERT INTO members (name, email, phone, member_type) VALUES
('Alice Johnson', 'alice.johnson@university.edu', '555-0101', 'STUDENT'),
('Bob Smith', 'bob.smith@university.edu', '555-0102', 'STUDENT'),
('Carol Williams', 'carol.williams@university.edu', '555-0103', 'STUDENT'),
('Dr. David Brown', 'david.brown@university.edu', '555-0201', 'FACULTY'),
('Dr. Emily Davis', 'emily.davis@university.edu', '555-0202', 'FACULTY'),
('Frank Miller', 'frank.miller@university.edu', '555-0104', 'STUDENT');

-- Sample transactions (2 active, 2 returned)
INSERT INTO transactions (book_id, member_id, issue_date, due_date, return_date, fine_amount, status) VALUES
(1, 1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 9 DAY), NULL, 0.00, 'ISSUED'),
(4, 4, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), NULL, 0.00, 'ISSUED'),
(2, 2, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 6 DAY), DATE_SUB(CURDATE(), INTERVAL 3 DAY), 1.50, 'RETURNED'),
(3, 3, DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(CURDATE(), INTERVAL 0 DAY), DATE_SUB(CURDATE(), INTERVAL 1 DAY), 0.00, 'RETURNED');

-- Update available counts for issued books
UPDATE books SET available = available - 1 WHERE book_id IN (1, 4);

-- Default librarian user (password: admin123 encoded with BCrypt)
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');
