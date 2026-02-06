package com.library.dao;

import com.library.model.Book;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Book entity.
 * Provides CRUD operations and search functionality for books.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class BookDAO {

    // SQL Queries
    private static final String INSERT_BOOK =
            "INSERT INTO books (isbn, title, author, publisher, quantity, available) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_BOOK =
            "UPDATE books SET isbn=?, title=?, author=?, publisher=?, quantity=?, available=? WHERE book_id=?";

    private static final String DELETE_BOOK =
            "DELETE FROM books WHERE book_id=?";

    private static final String SELECT_BY_ID =
            "SELECT * FROM books WHERE book_id=?";

    private static final String SELECT_BY_ISBN =
            "SELECT * FROM books WHERE isbn=?";

    private static final String SELECT_ALL =
            "SELECT * FROM books ORDER BY title";

    private static final String SEARCH_BOOKS =
            "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? ORDER BY title";

    private static final String SELECT_AVAILABLE =
            "SELECT * FROM books WHERE available > 0 ORDER BY title";

    private static final String UPDATE_AVAILABLE =
            "UPDATE books SET available=? WHERE book_id=?";

    private static final String DECREMENT_AVAILABLE =
            "UPDATE books SET available = available - 1 WHERE book_id=? AND available > 0";

    private static final String INCREMENT_AVAILABLE =
            "UPDATE books SET available = available + 1 WHERE book_id=? AND available < quantity";

    private static final String COUNT_BOOKS =
            "SELECT COUNT(*) FROM books";

    private static final String COUNT_AVAILABLE =
            "SELECT SUM(available) FROM books";

    private static final String COUNT_ISSUED =
            "SELECT SUM(quantity - available) FROM books";

    /**
     * Insert a new book into the database
     *
     * @param book the book to insert
     * @return true if successful
     */
    public boolean insert(Book book) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setInt(6, book.getAvailable());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBookId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update an existing book
     *
     * @param book the book to update
     * @return true if successful
     */
    public boolean update(Book book) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOK)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setInt(6, book.getAvailable());
            pstmt.setInt(7, book.getBookId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete a book by ID
     *
     * @param bookId the book ID
     * @return true if successful
     */
    public boolean delete(int bookId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BOOK)) {

            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Find a book by ID
     *
     * @param bookId the book ID
     * @return Book object or null if not found
     */
    public Book findById(int bookId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {

            pstmt.setInt(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding book by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find a book by ISBN
     *
     * @param isbn the ISBN
     * @return Book object or null if not found
     */
    public Book findByIsbn(String isbn) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ISBN)) {

            pstmt.setString(1, isbn);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding book by ISBN: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all books
     *
     * @return list of all books
     */
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Search books by title, author, or ISBN
     *
     * @param keyword the search keyword
     * @return list of matching books
     */
    public List<Book> search(String keyword) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_BOOKS)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRowToBook(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Get all available books (with at least one copy available)
     *
     * @return list of available books
     */
    public List<Book> findAvailable() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_AVAILABLE);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding available books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Decrement available count when a book is issued
     *
     * @param bookId the book ID
     * @return true if successful
     */
    public boolean decrementAvailable(int bookId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DECREMENT_AVAILABLE)) {

            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error decrementing available: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Increment available count when a book is returned
     *
     * @param bookId the book ID
     * @return true if successful
     */
    public boolean incrementAvailable(int bookId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INCREMENT_AVAILABLE)) {

            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error incrementing available: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get total count of books
     *
     * @return total number of book titles
     */
    public int getTotalBooks() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_BOOKS);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting books: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get total count of available copies
     *
     * @return total available copies
     */
    public int getTotalAvailable() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_AVAILABLE);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting available: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get total count of issued copies
     *
     * @return total issued copies
     */
    public int getTotalIssued() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_ISSUED);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting issued: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Map a ResultSet row to a Book object
     *
     * @param rs the ResultSet
     * @return Book object
     * @throws SQLException if mapping fails
     */
    private Book mapRowToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setQuantity(rs.getInt("quantity"));
        book.setAvailable(rs.getInt("available"));
        book.setCreatedAt(rs.getTimestamp("created_at"));
        return book;
    }
}
