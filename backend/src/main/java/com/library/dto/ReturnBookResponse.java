package com.library.dto;

import com.library.model.Transaction;
import java.math.BigDecimal;

public class ReturnBookResponse {
    private Transaction transaction;
    private BigDecimal fineAmount;
    private long overdueDays;

    public ReturnBookResponse() {}

    public ReturnBookResponse(Transaction transaction, BigDecimal fineAmount, long overdueDays) {
        this.transaction = transaction;
        this.fineAmount = fineAmount;
        this.overdueDays = overdueDays;
    }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    public long getOverdueDays() { return overdueDays; }
    public void setOverdueDays(long overdueDays) { this.overdueDays = overdueDays; }
}
