package com.library.dao;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.util.DateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for TransactionDAO class.
 * Note: These tests require a running MySQL database with the library_db schema.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class TransactionDAOTest {

    private TransactionDAO transactionDAO;
    private BookDAO bookDAO;
    private MemberDAO memberDAO;

    private Book testBook;
    private Member testMember;
    private int originalAvailable;

    @Before
    public void setUp() {
        transactionDAO = new TransactionDAO();
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();

        // Create a test book
        testBook = new Book(
                "TRANS-TEST-" + System.currentTimeMillis(),
                "Transaction Test Book",
                "Test Author",
                "Test Publisher",
                5
        );
        bookDAO.insert(testBook);
        originalAvailable = testBook.getAvailable();

        // Create a test member
        testMember = new Member(
                "Transaction Test Member",
                "trans.test" + System.currentTimeMillis() + "@test.com",
                "555-8888",
                "student"
        );
        memberDAO.insert(testMember);
    }

    @After
    public void tearDown() {
        // Clean up
        if (testBook != null && testBook.getBookId() > 0) {
            bookDAO.delete(testBook.getBookId());
        }
        if (testMember != null && testMember.getMemberId() > 0) {
            memberDAO.delete(testMember.getMemberId());
        }
    }

    @Test
    public void testIssueBook() {
        boolean result = transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());
        assertTrue("Issue book should return true", result);

        // Verify book available count decreased
        Book updatedBook = bookDAO.findById(testBook.getBookId());
        assertEquals("Available should be decremented", originalAvailable - 1, updatedBook.getAvailable());
    }

    @Test
    public void testIssueBookNotAvailable() {
        // Create a book with 0 available
        Book unavailableBook = new Book(
                "UNAVAIL-" + System.currentTimeMillis(),
                "Unavailable Book",
                "Author",
                "Publisher",
                1
        );
        unavailableBook.setAvailable(0);
        bookDAO.insert(unavailableBook);

        boolean result = transactionDAO.issueBook(unavailableBook.getBookId(), testMember.getMemberId());
        assertFalse("Issue should fail for unavailable book", result);

        // Clean up
        bookDAO.delete(unavailableBook.getBookId());
    }

    @Test
    public void testReturnBook() {
        // First issue the book
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        // Find the transaction
        List<Transaction> active = transactionDAO.findActiveByMember(testMember.getMemberId());
        assertFalse("Should have active transactions", active.isEmpty());

        Transaction transaction = active.get(0);

        // Return the book
        BigDecimal fine = transactionDAO.returnBook(transaction.getTransactionId());
        assertTrue("Fine should be >= 0", fine.compareTo(BigDecimal.ZERO) >= 0);

        // Verify book available count increased
        Book updatedBook = bookDAO.findById(testBook.getBookId());
        assertEquals("Available should be back to original", originalAvailable, updatedBook.getAvailable());

        // Verify transaction is marked as returned
        Transaction returned = transactionDAO.findById(transaction.getTransactionId());
        assertTrue("Transaction should be returned", returned.isReturned());
    }

    @Test
    public void testFindById() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        List<Transaction> active = transactionDAO.findActiveByMember(testMember.getMemberId());
        Transaction transaction = active.get(0);

        Transaction found = transactionDAO.findById(transaction.getTransactionId());
        assertNotNull("Found transaction should not be null", found);
        assertEquals("Book ID should match", testBook.getBookId(), found.getBookId());
        assertEquals("Member ID should match", testMember.getMemberId(), found.getMemberId());
    }

    @Test
    public void testFindActive() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        List<Transaction> active = transactionDAO.findActive();
        assertNotNull("Active list should not be null", active);
        assertTrue("Should have at least one active transaction", active.size() > 0);
    }

    @Test
    public void testFindByMember() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        List<Transaction> memberTransactions = transactionDAO.findByMember(testMember.getMemberId());
        assertNotNull("Member transactions should not be null", memberTransactions);
        assertTrue("Should have at least one transaction", memberTransactions.size() > 0);
    }

    @Test
    public void testFindActiveByMember() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        List<Transaction> active = transactionDAO.findActiveByMember(testMember.getMemberId());
        assertNotNull("Active list should not be null", active);
        assertEquals("Should have one active transaction", 1, active.size());
    }

    @Test
    public void testFindByBook() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        List<Transaction> bookTransactions = transactionDAO.findByBook(testBook.getBookId());
        assertNotNull("Book transactions should not be null", bookTransactions);
        assertTrue("Should have at least one transaction", bookTransactions.size() > 0);
    }

    @Test
    public void testCountActiveByMember() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        int count = transactionDAO.countActiveByMember(testMember.getMemberId());
        assertEquals("Should have one active issue", 1, count);
    }

    @Test
    public void testCountActive() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        int count = transactionDAO.countActive();
        assertTrue("Should have at least one active issue", count >= 1);
    }

    @Test
    public void testFindActiveWithDetails() {
        transactionDAO.issueBook(testBook.getBookId(), testMember.getMemberId());

        List<Transaction> active = transactionDAO.findActiveWithDetails();
        assertNotNull("Active list should not be null", active);
        assertTrue("Should have at least one active transaction", active.size() > 0);

        // Verify details are loaded
        Transaction trans = null;
        for (Transaction t : active) {
            if (t.getBookId() == testBook.getBookId()) {
                trans = t;
                break;
            }
        }

        if (trans != null) {
            assertNotNull("Book details should be loaded", trans.getBook());
            assertNotNull("Member details should be loaded", trans.getMember());
        }
    }

    @Test
    public void testTransactionIsIssued() {
        Transaction trans = new Transaction();
        trans.setStatus("issued");

        assertTrue("isIssued should return true", trans.isIssued());
        assertFalse("isReturned should return false", trans.isReturned());
    }

    @Test
    public void testTransactionIsReturned() {
        Transaction trans = new Transaction();
        trans.setStatus("returned");

        assertTrue("isReturned should return true", trans.isReturned());
        assertFalse("isIssued should return false", trans.isIssued());
    }

    @Test
    public void testTransactionIsOverdue() {
        Transaction trans = new Transaction();
        trans.setStatus("issued");

        // Set due date to yesterday
        Date yesterday = DateUtil.addDays(DateUtil.getCurrentDate(), -1);
        trans.setDueDate(yesterday);

        assertTrue("Should be overdue", trans.isOverdue());
        assertTrue("Overdue days should be positive", trans.getOverdueDays() > 0);
    }

    @Test
    public void testTransactionNotOverdue() {
        Transaction trans = new Transaction();
        trans.setStatus("issued");

        // Set due date to tomorrow
        Date tomorrow = DateUtil.addDays(DateUtil.getCurrentDate(), 1);
        trans.setDueDate(tomorrow);

        assertFalse("Should not be overdue", trans.isOverdue());
        assertEquals("Overdue days should be 0", 0, trans.getOverdueDays());
    }
}
