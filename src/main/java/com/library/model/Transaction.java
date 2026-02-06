package com.library.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Transaction entity class representing a book issue/return transaction.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private int transactionId;
    private int bookId;
    private int memberId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private BigDecimal fineAmount;
    private String status; // 'issued' or 'returned'

    // Associated objects for display purposes
    private Book book;
    private Member member;

    // Default constructor
    public Transaction() {
        this.fineAmount = BigDecimal.ZERO;
        this.status = "issued";
    }

    // Constructor for issuing a book
    public Transaction(int bookId, int memberId, Date issueDate, Date dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.fineAmount = BigDecimal.ZERO;
        this.status = "issued";
    }

    // Full constructor
    public Transaction(int transactionId, int bookId, int memberId, Date issueDate,
                       Date dueDate, Date returnDate, BigDecimal fineAmount, String status) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount != null ? fineAmount : BigDecimal.ZERO;
        this.status = status;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Check if the transaction is currently active (book is issued)
     * @return true if status is 'issued'
     */
    public boolean isIssued() {
        return "issued".equalsIgnoreCase(status);
    }

    /**
     * Check if the transaction is completed (book is returned)
     * @return true if status is 'returned'
     */
    public boolean isReturned() {
        return "returned".equalsIgnoreCase(status);
    }

    /**
     * Check if the book is overdue
     * @return true if due date has passed and book is not returned
     */
    public boolean isOverdue() {
        if (isReturned()) {
            return false;
        }
        Date today = new Date(System.currentTimeMillis());
        return dueDate != null && today.after(dueDate);
    }

    /**
     * Get the number of overdue days
     * @return number of days overdue (0 if not overdue)
     */
    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        Date today = new Date(System.currentTimeMillis());
        long diffInMillies = today.getTime() - dueDate.getTime();
        return diffInMillies / (1000 * 60 * 60 * 24);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", fineAmount=" + fineAmount +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(transactionId);
    }
}
