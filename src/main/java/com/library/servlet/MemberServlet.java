package com.library.servlet;

import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.model.Member;
import com.library.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling member management operations.
 * Supports register, update, delete, search, and list operations.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
@WebServlet(name = "MemberServlet", urlPatterns = {"/members"})
public class MemberServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private MemberDAO memberDAO;
    private TransactionDAO transactionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        memberDAO = new MemberDAO();
        transactionDAO = new TransactionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteMember(request, response);
                    break;
                case "search":
                    searchMembers(request, response);
                    break;
                case "view":
                    viewMember(request, response);
                    break;
                case "list":
                default:
                    listMembers(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            listMembers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "register":
                    registerMember(request, response);
                    break;
                case "update":
                    updateMember(request, response);
                    break;
                default:
                    listMembers(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            listMembers(request, response);
        }
    }

    /**
     * Display list of all members
     */
    private void listMembers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Member> members = memberDAO.findAll();
        request.setAttribute("members", members);
        request.getRequestDispatcher("/members.jsp").forward(request, response);
    }

    /**
     * Show form for registering a new member
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "register");
        request.getRequestDispatcher("/members.jsp").forward(request, response);
    }

    /**
     * Show form for editing an existing member
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int memberId = Integer.parseInt(request.getParameter("id"));
        Member member = memberDAO.findById(memberId);

        if (member != null) {
            request.setAttribute("member", member);
            request.setAttribute("action", "edit");
        } else {
            request.setAttribute("error", "Member not found!");
        }

        List<Member> members = memberDAO.findAll();
        request.setAttribute("members", members);
        request.getRequestDispatcher("/members.jsp").forward(request, response);
    }

    /**
     * View member details with their issued books
     */
    private void viewMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int memberId = Integer.parseInt(request.getParameter("id"));
        Member member = memberDAO.findById(memberId);

        if (member != null) {
            request.setAttribute("viewMember", member);
            // Get member's active transactions
            List<Transaction> transactions = transactionDAO.findActiveByMember(memberId);
            request.setAttribute("memberTransactions", transactions);
        } else {
            request.setAttribute("error", "Member not found!");
        }

        List<Member> members = memberDAO.findAll();
        request.setAttribute("members", members);
        request.getRequestDispatcher("/members.jsp").forward(request, response);
    }

    /**
     * Register a new member
     */
    private void registerMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String memberType = request.getParameter("memberType");

        Member member = new Member(name, email, phone, memberType);

        if (memberDAO.insert(member)) {
            request.setAttribute("success", "Member registered successfully! ID: " + member.getMemberId());
        } else {
            request.setAttribute("error", "Failed to register member.");
        }

        listMembers(request, response);
    }

    /**
     * Update an existing member
     */
    private void updateMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int memberId = Integer.parseInt(request.getParameter("memberId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String memberType = request.getParameter("memberType");

        Member member = new Member();
        member.setMemberId(memberId);
        member.setName(name);
        member.setEmail(email);
        member.setPhone(phone);
        member.setMemberType(memberType);

        if (memberDAO.update(member)) {
            request.setAttribute("success", "Member updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update member.");
        }

        listMembers(request, response);
    }

    /**
     * Delete a member
     */
    private void deleteMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int memberId = Integer.parseInt(request.getParameter("id"));

        // Check if member has active transactions
        int activeCount = transactionDAO.countActiveByMember(memberId);
        if (activeCount > 0) {
            request.setAttribute("error", "Cannot delete member. They have " + activeCount + " book(s) not returned.");
            listMembers(request, response);
            return;
        }

        if (memberDAO.delete(memberId)) {
            request.setAttribute("success", "Member deleted successfully!");
        } else {
            request.setAttribute("error", "Failed to delete member.");
        }

        listMembers(request, response);
    }

    /**
     * Search members by keyword
     */
    private void searchMembers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

        List<Member> members;
        if (keyword != null && !keyword.trim().isEmpty()) {
            members = memberDAO.search(keyword.trim());
            request.setAttribute("keyword", keyword);
        } else {
            members = memberDAO.findAll();
        }

        request.setAttribute("members", members);
        request.getRequestDispatcher("/members.jsp").forward(request, response);
    }
}
