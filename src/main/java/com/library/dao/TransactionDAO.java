package com.library.dao;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.util.DBConnection;
import com.library.util.DateUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction entity.
 * Provides operations for book issue/return transactions.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class TransactionDAO {

    // SQL Queries
    private static final String INSERT_TRANSACTION =
            "INSERT INTO transactions (book_id, member_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, 'issued')";

    private static final String UPDATE_RETURN =
            "UPDATE transactions SET return_date=?, fine_amount=?, status='returned' WHERE transaction_id=?";

    private static final String SELECT_BY_ID =
            "SELECT * FROM transactions WHERE transaction_id=?";

    private static final String SELECT_ALL =
            "SELECT * FROM transactions ORDER BY issue_date DESC";

    private static final String SELECT_ACTIVE =
            "SELECT * FROM transactions WHERE status='issued' ORDER BY due_date";

    private static final String SELECT_OVERDUE =
            "SELECT * FROM transactions WHERE status='issued' AND due_date < CURDATE() ORDER BY due_date";

    private static final String SELECT_BY_MEMBER =
            "SELECT * FROM transactions WHERE member_id=? ORDER BY issue_date DESC";

    private static final String SELECT_ACTIVE_BY_MEMBER =
            "SELECT * FROM transactions WHERE member_id=? AND status='issued'";

    private static final String SELECT_BY_BOOK =
            "SELECT * FROM transactions WHERE book_id=? ORDER BY issue_date DESC";

    private static final String SELECT_ACTIVE_BY_BOOK_AND_MEMBER =
            "SELECT * FROM transactions WHERE book_id=? AND member_id=? AND status='issued'";

    private static final String COUNT_ACTIVE_BY_MEMBER =
            "SELECT COUNT(*) FROM transactions WHERE member_id=? AND status='issued'";

    private static final String COUNT_ACTIVE =
            "SELECT COUNT(*) FROM transactions WHERE status='issued'";

    private static final String COUNT_OVERDUE =
            "SELECT COUNT(*) FROM transactions WHERE status='issued' AND due_date < CURDATE()";

    private static final String SELECT_WITH_DETAILS =
            "SELECT t.*, b.title as book_title, b.isbn, b.author, m.name as member_name, m.email, m.member_type " +
                    "FROM transactions t " +
                    "JOIN books b ON t.book_id = b.book_id " +
                    "JOIN members m ON t.member_id = m.member_id " +
                    "WHERE t.status='issued' ORDER BY t.due_date";

    private static final String SELECT_OVERDUE_WITH_DETAILS =
            "SELECT t.*, b.title as book_title, b.isbn, b.author, m.name as member_name, m.email, m.member_type " +
                    "FROM transactions t " +
                    "JOIN books b ON t.book_id = b.book_id " +
                    "JOIN members m ON t.member_id = m.member_id " +
                    "WHERE t.status='issued' AND t.due_date < CURDATE() ORDER BY t.due_date";

    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();

    /**
     * Issue a book to a member
     *
     * @param bookId   the book ID
     * @param memberId the member ID
     * @return true if successful
     */
    public boolean issueBook(int bookId, int memberId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Check if book is available
            Book book = bookDAO.findById(bookId);
            if (book == null || book.getAvailable() <= 0) {
                return false;
            }

            // Get member for loan period calculation
            Member member = memberDAO.findById(memberId);
            if (member == null) {
                return false;
            }

            // Calculate dates
            java.sql.Date issueDate = DateUtil.getCurrentDate();
            int loanPeriod = member.isFaculty() ?
                    DateUtil.DEFAULT_LOAN_PERIOD_FACULTY : DateUtil.DEFAULT_LOAN_PERIOD_STUDENT;
            java.sql.Date dueDate = DateUtil.calculateDueDate(issueDate, loanPeriod);

            // Insert transaction
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, bookId);
                pstmt.setInt(2, memberId);
                pstmt.setDate(3, issueDate);
                pstmt.setDate(4, dueDate);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Decrement available count
                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE books SET available = available - 1 WHERE book_id=? AND available > 0")) {
                        updateStmt.setInt(1, bookId);
                        updateStmt.executeUpdate();
                    }

                    conn.commit();
                    return true;
                }
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            System.err.println("Error issuing book: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return a book
     *
     * @param transactionId the transaction ID
     * @return the fine amount (0 if no fine)
     */
    public BigDecimal returnBook(int transactionId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Get the transaction
            Transaction transaction = findById(transactionId);
            if (transaction == null || transaction.isReturned()) {
                return BigDecimal.valueOf(-1); // Invalid transaction
            }

            // Calculate fine
            java.sql.Date returnDate = DateUtil.getCurrentDate();
            BigDecimal fine = DateUtil.calculateFine(transaction.getDueDate(), returnDate);

            // Update transaction
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_RETURN)) {
                pstmt.setDate(1, returnDate);
                pstmt.setBigDecimal(2, fine);
                pstmt.setInt(3, transactionId);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Increment available count
                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE books SET available = available + 1 WHERE book_id=? AND available < quantity")) {
                        updateStmt.setInt(1, transaction.getBookId());
                        updateStmt.executeUpdate();
                    }

                    conn.commit();
                    return fine;
                }
            }

            conn.rollback();
            return BigDecimal.valueOf(-1);

        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return BigDecimal.valueOf(-1);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Find a transaction by ID
     *
     * @param transactionId the transaction ID
     * @return Transaction object or null if not found
     */
    public Transaction findById(int transactionId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {

            pstmt.setInt(1, transactionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTransaction(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding transaction by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all transactions
     *
     * @return list of all transactions
     */
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get all active (issued) transactions
     *
     * @return list of active transactions
     */
    public List<Transaction> findActive() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ACTIVE);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Transaction t = mapRowToTransaction(rs);
                // Load associated book and member
                t.setBook(bookDAO.findById(t.getBookId()));
                t.setMember(memberDAO.findById(t.getMemberId()));
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error finding active transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get all overdue transactions
     *
     * @return list of overdue transactions
     */
    public List<Transaction> findOverdue() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_OVERDUE);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Transaction t = mapRowToTransaction(rs);
                // Load associated book and member
                t.setBook(bookDAO.findById(t.getBookId()));
                t.setMember(memberDAO.findById(t.getMemberId()));
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error finding overdue transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get transactions by member
     *
     * @param memberId the member ID
     * @return list of transactions for the member
     */
    public List<Transaction> findByMember(int memberId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_MEMBER)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction t = mapRowToTransaction(rs);
                    t.setBook(bookDAO.findById(t.getBookId()));
                    transactions.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding transactions by member: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get active transactions by member
     *
     * @param memberId the member ID
     * @return list of active transactions for the member
     */
    public List<Transaction> findActiveByMember(int memberId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ACTIVE_BY_MEMBER)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction t = mapRowToTransaction(rs);
                    t.setBook(bookDAO.findById(t.getBookId()));
                    transactions.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding active transactions by member: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get transactions by book
     *
     * @param bookId the book ID
     * @return list of transactions for the book
     */
    public List<Transaction> findByBook(int bookId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_BOOK)) {

            pstmt.setInt(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction t = mapRowToTransaction(rs);
                    t.setMember(memberDAO.findById(t.getMemberId()));
                    transactions.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding transactions by book: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Count active issues by a member
     *
     * @param memberId the member ID
     * @return count of active issues
     */
    public int countActiveByMember(int memberId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_ACTIVE_BY_MEMBER)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting active by member: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get total count of active issues
     *
     * @return count of active issues
     */
    public int countActive() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_ACTIVE);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting active: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get total count of overdue issues
     *
     * @return count of overdue issues
     */
    public int countOverdue() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_OVERDUE);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting overdue: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get active transactions with book and member details
     *
     * @return list of transactions with details
     */
    public List<Transaction> findActiveWithDetails() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_WITH_DETAILS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Transaction t = mapRowToTransaction(rs);

                // Create and set Book
                Book book = new Book();
                book.setBookId(t.getBookId());
                book.setTitle(rs.getString("book_title"));
                book.setIsbn(rs.getString("isbn"));
                book.setAuthor(rs.getString("author"));
                t.setBook(book);

                // Create and set Member
                Member member = new Member();
                member.setMemberId(t.getMemberId());
                member.setName(rs.getString("member_name"));
                member.setEmail(rs.getString("email"));
                member.setMemberType(rs.getString("member_type"));
                t.setMember(member);

                transactions.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error finding active with details: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get overdue transactions with book and member details
     *
     * @return list of overdue transactions with details
     */
    public List<Transaction> findOverdueWithDetails() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_OVERDUE_WITH_DETAILS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Transaction t = mapRowToTransaction(rs);

                // Create and set Book
                Book book = new Book();
                book.setBookId(t.getBookId());
                book.setTitle(rs.getString("book_title"));
                book.setIsbn(rs.getString("isbn"));
                book.setAuthor(rs.getString("author"));
                t.setBook(book);

                // Create and set Member
                Member member = new Member();
                member.setMemberId(t.getMemberId());
                member.setName(rs.getString("member_name"));
                member.setEmail(rs.getString("email"));
                member.setMemberType(rs.getString("member_type"));
                t.setMember(member);

                transactions.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error finding overdue with details: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Map a ResultSet row to a Transaction object
     *
     * @param rs the ResultSet
     * @return Transaction object
     * @throws SQLException if mapping fails
     */
    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setBookId(rs.getInt("book_id"));
        transaction.setMemberId(rs.getInt("member_id"));
        transaction.setIssueDate(rs.getDate("issue_date"));
        transaction.setDueDate(rs.getDate("due_date"));
        transaction.setReturnDate(rs.getDate("return_date"));
        transaction.setFineAmount(rs.getBigDecimal("fine_amount"));
        transaction.setStatus(rs.getString("status"));
        return transaction;
    }
}
