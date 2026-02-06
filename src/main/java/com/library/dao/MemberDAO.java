package com.library.dao;

import com.library.model.Member;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Member entity.
 * Provides CRUD operations and search functionality for members.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class MemberDAO {

    // SQL Queries
    private static final String INSERT_MEMBER =
            "INSERT INTO members (name, email, phone, member_type) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_MEMBER =
            "UPDATE members SET name=?, email=?, phone=?, member_type=? WHERE member_id=?";

    private static final String DELETE_MEMBER =
            "DELETE FROM members WHERE member_id=?";

    private static final String SELECT_BY_ID =
            "SELECT * FROM members WHERE member_id=?";

    private static final String SELECT_BY_EMAIL =
            "SELECT * FROM members WHERE email=?";

    private static final String SELECT_ALL =
            "SELECT * FROM members ORDER BY name";

    private static final String SEARCH_MEMBERS =
            "SELECT * FROM members WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? ORDER BY name";

    private static final String SELECT_BY_TYPE =
            "SELECT * FROM members WHERE member_type=? ORDER BY name";

    private static final String COUNT_MEMBERS =
            "SELECT COUNT(*) FROM members";

    private static final String COUNT_BY_TYPE =
            "SELECT COUNT(*) FROM members WHERE member_type=?";

    /**
     * Insert a new member into the database
     *
     * @param member the member to insert
     * @return true if successful
     */
    public boolean insert(Member member) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getMemberType());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        member.setMemberId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update an existing member
     *
     * @param member the member to update
     * @return true if successful
     */
    public boolean update(Member member) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_MEMBER)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getMemberType());
            pstmt.setInt(5, member.getMemberId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete a member by ID
     *
     * @param memberId the member ID
     * @return true if successful
     */
    public boolean delete(int memberId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_MEMBER)) {

            pstmt.setInt(1, memberId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Find a member by ID
     *
     * @param memberId the member ID
     * @return Member object or null if not found
     */
    public Member findById(int memberId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMember(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding member by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find a member by email
     *
     * @param email the email address
     * @return Member object or null if not found
     */
    public Member findByEmail(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_EMAIL)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMember(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding member by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all members
     *
     * @return list of all members
     */
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapRowToMember(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all members: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }

    /**
     * Search members by name, email, or phone
     *
     * @param keyword the search keyword
     * @return list of matching members
     */
    public List<Member> search(String keyword) {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_MEMBERS)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapRowToMember(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching members: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }

    /**
     * Find members by type (student or faculty)
     *
     * @param memberType the member type
     * @return list of members of specified type
     */
    public List<Member> findByType(String memberType) {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_TYPE)) {

            pstmt.setString(1, memberType);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapRowToMember(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding members by type: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }

    /**
     * Get total count of members
     *
     * @return total number of members
     */
    public int getTotalMembers() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_MEMBERS);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting members: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get count of members by type
     *
     * @param memberType the member type
     * @return count of members of specified type
     */
    public int getCountByType(String memberType) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_BY_TYPE)) {

            pstmt.setString(1, memberType);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting members by type: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Map a ResultSet row to a Member object
     *
     * @param rs the ResultSet
     * @return Member object
     * @throws SQLException if mapping fails
     */
    private Member mapRowToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setMemberType(rs.getString("member_type"));
        member.setCreatedAt(rs.getTimestamp("created_at"));
        return member;
    }
}
