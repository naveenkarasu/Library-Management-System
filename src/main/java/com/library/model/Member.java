package com.library.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Member entity class representing a library member (student or faculty).
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    private int memberId;
    private String name;
    private String email;
    private String phone;
    private String memberType; // 'student' or 'faculty'
    private Timestamp createdAt;

    // Default constructor
    public Member() {
    }

    // Constructor with essential fields
    public Member(String name, String email, String phone, String memberType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberType = memberType;
    }

    // Full constructor
    public Member(int memberId, String name, String email, String phone,
                  String memberType, Timestamp createdAt) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberType = memberType;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Check if member is faculty
     * @return true if member is faculty
     */
    public boolean isFaculty() {
        return "faculty".equalsIgnoreCase(memberType);
    }

    /**
     * Check if member is student
     * @return true if member is student
     */
    public boolean isStudent() {
        return "student".equalsIgnoreCase(memberType);
    }

    /**
     * Get the maximum number of books this member can borrow
     * Faculty can borrow more books than students
     * @return maximum number of books
     */
    public int getMaxBooks() {
        return isFaculty() ? 10 : 5;
    }

    /**
     * Get the loan period in days for this member
     * Faculty gets longer loan period
     * @return loan period in days
     */
    public int getLoanPeriodDays() {
        return isFaculty() ? 30 : 14;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", memberType='" + memberType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return memberId == member.memberId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(memberId);
    }
}
