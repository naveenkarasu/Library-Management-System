package com.library.dto;

public class DashboardStats {
    private long totalBooks;
    private long totalMembers;
    private long activeTransactions;
    private long overdueBooks;
    private long totalCopies;
    private long availableCopies;

    public DashboardStats() {}

    public DashboardStats(long totalBooks, long totalMembers, long activeTransactions,
                          long overdueBooks, long totalCopies, long availableCopies) {
        this.totalBooks = totalBooks;
        this.totalMembers = totalMembers;
        this.activeTransactions = activeTransactions;
        this.overdueBooks = overdueBooks;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }
    public long getTotalMembers() { return totalMembers; }
    public void setTotalMembers(long totalMembers) { this.totalMembers = totalMembers; }
    public long getActiveTransactions() { return activeTransactions; }
    public void setActiveTransactions(long activeTransactions) { this.activeTransactions = activeTransactions; }
    public long getOverdueBooks() { return overdueBooks; }
    public void setOverdueBooks(long overdueBooks) { this.overdueBooks = overdueBooks; }
    public long getTotalCopies() { return totalCopies; }
    public void setTotalCopies(long totalCopies) { this.totalCopies = totalCopies; }
    public long getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(long availableCopies) { this.availableCopies = availableCopies; }
}
