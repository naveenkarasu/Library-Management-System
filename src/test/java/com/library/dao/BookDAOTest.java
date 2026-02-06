package com.library.dao;

import com.library.model.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for BookDAO class.
 * Note: These tests require a running MySQL database with the library_db schema.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class BookDAOTest {

    private BookDAO bookDAO;
    private Book testBook;

    @Before
    public void setUp() {
        bookDAO = new BookDAO();
        // Create a test book with unique ISBN
        testBook = new Book(
                "TEST-" + System.currentTimeMillis(),
                "Test Book Title",
                "Test Author",
                "Test Publisher",
                5
        );
    }

    @After
    public void tearDown() {
        // Clean up test book if it was created
        if (testBook != null && testBook.getBookId() > 0) {
            bookDAO.delete(testBook.getBookId());
        }
    }

    @Test
    public void testInsert() {
        boolean result = bookDAO.insert(testBook);
        assertTrue("Insert should return true", result);
        assertTrue("Book ID should be set after insert", testBook.getBookId() > 0);
    }

    @Test
    public void testFindById() {
        bookDAO.insert(testBook);

        Book found = bookDAO.findById(testBook.getBookId());
        assertNotNull("Found book should not be null", found);
        assertEquals("Title should match", testBook.getTitle(), found.getTitle());
        assertEquals("Author should match", testBook.getAuthor(), found.getAuthor());
    }

    @Test
    public void testFindByIdNotFound() {
        Book found = bookDAO.findById(99999);
        assertNull("Should return null for non-existent ID", found);
    }

    @Test
    public void testFindByIsbn() {
        bookDAO.insert(testBook);

        Book found = bookDAO.findByIsbn(testBook.getIsbn());
        assertNotNull("Found book should not be null", found);
        assertEquals("ISBN should match", testBook.getIsbn(), found.getIsbn());
    }

    @Test
    public void testUpdate() {
        bookDAO.insert(testBook);

        testBook.setTitle("Updated Title");
        testBook.setAuthor("Updated Author");
        boolean result = bookDAO.update(testBook);

        assertTrue("Update should return true", result);

        Book updated = bookDAO.findById(testBook.getBookId());
        assertEquals("Title should be updated", "Updated Title", updated.getTitle());
        assertEquals("Author should be updated", "Updated Author", updated.getAuthor());
    }

    @Test
    public void testDelete() {
        bookDAO.insert(testBook);
        int bookId = testBook.getBookId();

        boolean result = bookDAO.delete(bookId);
        assertTrue("Delete should return true", result);

        Book deleted = bookDAO.findById(bookId);
        assertNull("Book should be null after delete", deleted);

        // Prevent double delete in tearDown
        testBook.setBookId(0);
    }

    @Test
    public void testFindAll() {
        bookDAO.insert(testBook);

        List<Book> books = bookDAO.findAll();
        assertNotNull("Book list should not be null", books);
        assertTrue("Book list should not be empty", books.size() > 0);
    }

    @Test
    public void testSearch() {
        bookDAO.insert(testBook);

        List<Book> results = bookDAO.search("Test Book");
        assertNotNull("Search results should not be null", results);
        assertTrue("Search should find at least one book", results.size() > 0);
    }

    @Test
    public void testSearchByAuthor() {
        bookDAO.insert(testBook);

        List<Book> results = bookDAO.search("Test Author");
        assertNotNull("Search results should not be null", results);
        assertTrue("Search by author should find books", results.size() > 0);
    }

    @Test
    public void testFindAvailable() {
        bookDAO.insert(testBook);

        List<Book> availableBooks = bookDAO.findAvailable();
        assertNotNull("Available books list should not be null", availableBooks);

        // Our test book should be in the list since it has available > 0
        boolean found = false;
        for (Book book : availableBooks) {
            if (book.getBookId() == testBook.getBookId()) {
                found = true;
                break;
            }
        }
        assertTrue("Test book should be in available list", found);
    }

    @Test
    public void testDecrementAvailable() {
        bookDAO.insert(testBook);
        int originalAvailable = testBook.getAvailable();

        boolean result = bookDAO.decrementAvailable(testBook.getBookId());
        assertTrue("Decrement should return true", result);

        Book updated = bookDAO.findById(testBook.getBookId());
        assertEquals("Available should be decremented", originalAvailable - 1, updated.getAvailable());
    }

    @Test
    public void testIncrementAvailable() {
        bookDAO.insert(testBook);

        // First decrement
        bookDAO.decrementAvailable(testBook.getBookId());

        // Then increment
        boolean result = bookDAO.incrementAvailable(testBook.getBookId());
        assertTrue("Increment should return true", result);

        Book updated = bookDAO.findById(testBook.getBookId());
        assertEquals("Available should be back to original", testBook.getAvailable(), updated.getAvailable());
    }

    @Test
    public void testGetTotalBooks() {
        bookDAO.insert(testBook);

        int total = bookDAO.getTotalBooks();
        assertTrue("Total books should be at least 1", total >= 1);
    }
}
