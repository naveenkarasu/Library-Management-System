package com.library.dao;

import com.library.model.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for MemberDAO class.
 * Note: These tests require a running MySQL database with the library_db schema.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class MemberDAOTest {

    private MemberDAO memberDAO;
    private Member testMember;

    @Before
    public void setUp() {
        memberDAO = new MemberDAO();
        // Create a test member with unique email
        testMember = new Member(
                "Test Member",
                "test" + System.currentTimeMillis() + "@test.com",
                "555-9999",
                "student"
        );
    }

    @After
    public void tearDown() {
        // Clean up test member if it was created
        if (testMember != null && testMember.getMemberId() > 0) {
            memberDAO.delete(testMember.getMemberId());
        }
    }

    @Test
    public void testInsert() {
        boolean result = memberDAO.insert(testMember);
        assertTrue("Insert should return true", result);
        assertTrue("Member ID should be set after insert", testMember.getMemberId() > 0);
    }

    @Test
    public void testFindById() {
        memberDAO.insert(testMember);

        Member found = memberDAO.findById(testMember.getMemberId());
        assertNotNull("Found member should not be null", found);
        assertEquals("Name should match", testMember.getName(), found.getName());
        assertEquals("Email should match", testMember.getEmail(), found.getEmail());
    }

    @Test
    public void testFindByIdNotFound() {
        Member found = memberDAO.findById(99999);
        assertNull("Should return null for non-existent ID", found);
    }

    @Test
    public void testFindByEmail() {
        memberDAO.insert(testMember);

        Member found = memberDAO.findByEmail(testMember.getEmail());
        assertNotNull("Found member should not be null", found);
        assertEquals("Email should match", testMember.getEmail(), found.getEmail());
    }

    @Test
    public void testUpdate() {
        memberDAO.insert(testMember);

        testMember.setName("Updated Name");
        testMember.setPhone("555-1111");
        boolean result = memberDAO.update(testMember);

        assertTrue("Update should return true", result);

        Member updated = memberDAO.findById(testMember.getMemberId());
        assertEquals("Name should be updated", "Updated Name", updated.getName());
        assertEquals("Phone should be updated", "555-1111", updated.getPhone());
    }

    @Test
    public void testDelete() {
        memberDAO.insert(testMember);
        int memberId = testMember.getMemberId();

        boolean result = memberDAO.delete(memberId);
        assertTrue("Delete should return true", result);

        Member deleted = memberDAO.findById(memberId);
        assertNull("Member should be null after delete", deleted);

        // Prevent double delete in tearDown
        testMember.setMemberId(0);
    }

    @Test
    public void testFindAll() {
        memberDAO.insert(testMember);

        List<Member> members = memberDAO.findAll();
        assertNotNull("Member list should not be null", members);
        assertTrue("Member list should not be empty", members.size() > 0);
    }

    @Test
    public void testSearch() {
        memberDAO.insert(testMember);

        List<Member> results = memberDAO.search("Test Member");
        assertNotNull("Search results should not be null", results);
        assertTrue("Search should find at least one member", results.size() > 0);
    }

    @Test
    public void testSearchByEmail() {
        memberDAO.insert(testMember);

        // Search by partial email
        List<Member> results = memberDAO.search("test.com");
        assertNotNull("Search results should not be null", results);
    }

    @Test
    public void testFindByTypeStudent() {
        memberDAO.insert(testMember);

        List<Member> students = memberDAO.findByType("student");
        assertNotNull("Student list should not be null", students);
        assertTrue("Should find at least one student", students.size() > 0);

        // Verify all results are students
        for (Member member : students) {
            assertEquals("Member type should be student", "student", member.getMemberType());
        }
    }

    @Test
    public void testFindByTypeFaculty() {
        // Create a faculty member
        testMember.setMemberType("faculty");
        memberDAO.insert(testMember);

        List<Member> faculty = memberDAO.findByType("faculty");
        assertNotNull("Faculty list should not be null", faculty);

        // Verify all results are faculty
        for (Member member : faculty) {
            assertEquals("Member type should be faculty", "faculty", member.getMemberType());
        }
    }

    @Test
    public void testGetTotalMembers() {
        memberDAO.insert(testMember);

        int total = memberDAO.getTotalMembers();
        assertTrue("Total members should be at least 1", total >= 1);
    }

    @Test
    public void testGetCountByType() {
        memberDAO.insert(testMember);

        int studentCount = memberDAO.getCountByType("student");
        assertTrue("Student count should be at least 1", studentCount >= 1);
    }

    @Test
    public void testMemberIsStudent() {
        testMember.setMemberType("student");
        assertTrue("isStudent should return true", testMember.isStudent());
        assertFalse("isFaculty should return false", testMember.isFaculty());
    }

    @Test
    public void testMemberIsFaculty() {
        testMember.setMemberType("faculty");
        assertTrue("isFaculty should return true", testMember.isFaculty());
        assertFalse("isStudent should return false", testMember.isStudent());
    }

    @Test
    public void testMemberMaxBooks() {
        testMember.setMemberType("student");
        assertEquals("Student max books should be 5", 5, testMember.getMaxBooks());

        testMember.setMemberType("faculty");
        assertEquals("Faculty max books should be 10", 10, testMember.getMaxBooks());
    }

    @Test
    public void testMemberLoanPeriod() {
        testMember.setMemberType("student");
        assertEquals("Student loan period should be 14", 14, testMember.getLoanPeriodDays());

        testMember.setMemberType("faculty");
        assertEquals("Faculty loan period should be 30", 30, testMember.getLoanPeriodDays());
    }
}
