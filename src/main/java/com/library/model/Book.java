package com.library.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Book entity class representing a book in the library.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int quantity;
    private int available;
    private Timestamp createdAt;

    // Default constructor
    public Book() {
    }

    // Constructor with essential fields
    public Book(String isbn, String title, String author, String publisher, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.available = quantity;
    }

    // Full constructor
    public Book(int bookId, String isbn, String title, String author, String publisher,
                int quantity, int available, Timestamp createdAt) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.available = available;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Check if book is available for issue
     * @return true if at least one copy is available
     */
    public boolean isAvailable() {
        return available > 0;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", quantity=" + quantity +
                ", available=" + available +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(bookId);
    }
}
