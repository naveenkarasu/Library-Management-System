package com.library.model;

public enum MemberType {
    STUDENT(5, 14),
    FACULTY(10, 30);

    private final int maxBooks;
    private final int loanPeriodDays;

    MemberType(int maxBooks, int loanPeriodDays) {
        this.maxBooks = maxBooks;
        this.loanPeriodDays = loanPeriodDays;
    }

    public int getMaxBooks() { return maxBooks; }
    public int getLoanPeriodDays() { return loanPeriodDays; }
}
