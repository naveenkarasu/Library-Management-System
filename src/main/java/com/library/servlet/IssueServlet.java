package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet for handling book issue and return operations.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
@WebServlet(name = "IssueServlet", urlPatterns = {"/issue", "/return"})
public class IssueServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private TransactionDAO transactionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        transactionDAO = new TransactionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if ("/issue".equals(servletPath)) {
            showIssuePage(request, response);
        } else if ("/return".equals(servletPath)) {
            showReturnPage(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("issue".equals(action)) {
                issueBook(request, response);
            } else if ("return".equals(action)) {
                returnBook(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            if ("issue".equals(action)) {
                showIssuePage(request, response);
            } else {
                showReturnPage(request, response);
            }
        }
    }

    /**
     * Show the issue book page
     */
    private void showIssuePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get available books
        List<Book> availableBooks = bookDAO.findAvailable();
        request.setAttribute("availableBooks", availableBooks);

        // Get all members
        List<Member> members = memberDAO.findAll();
        request.setAttribute("members", members);

        request.getRequestDispatcher("/issue.jsp").forward(request, response);
    }

    /**
     * Show the return book page
     */
    private void showReturnPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get active transactions
        List<Transaction> activeTransactions = transactionDAO.findActiveWithDetails();
        request.setAttribute("activeTransactions", activeTransactions);

        request.getRequestDispatcher("/return.jsp").forward(request, response);
    }

    /**
     * Issue a book to a member
     */
    private void issueBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int memberId = Integer.parseInt(request.getParameter("memberId"));

        // Validate book availability
        Book book = bookDAO.findById(bookId);
        if (book == null) {
            request.setAttribute("error", "Book not found!");
            showIssuePage(request, response);
            return;
        }

        if (book.getAvailable() <= 0) {
            request.setAttribute("error", "No copies of this book are available!");
            showIssuePage(request, response);
            return;
        }

        // Validate member
        Member member = memberDAO.findById(memberId);
        if (member == null) {
            request.setAttribute("error", "Member not found!");
            showIssuePage(request, response);
            return;
        }

        // Check member's book limit
        int currentIssued = transactionDAO.countActiveByMember(memberId);
        if (currentIssued >= member.getMaxBooks()) {
            request.setAttribute("error", "Member has reached maximum book limit (" + member.getMaxBooks() + " books)!");
            showIssuePage(request, response);
            return;
        }

        // Issue the book
        if (transactionDAO.issueBook(bookId, memberId)) {
            request.setAttribute("success", "Book '" + book.getTitle() + "' issued successfully to " + member.getName() + "!");
        } else {
            request.setAttribute("error", "Failed to issue book. Please try again.");
        }

        showIssuePage(request, response);
    }

    /**
     * Return a book
     */
    private void returnBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int transactionId = Integer.parseInt(request.getParameter("transactionId"));

        // Get transaction details for display
        Transaction transaction = transactionDAO.findById(transactionId);
        if (transaction == null) {
            request.setAttribute("error", "Transaction not found!");
            showReturnPage(request, response);
            return;
        }

        if (transaction.isReturned()) {
            request.setAttribute("error", "This book has already been returned!");
            showReturnPage(request, response);
            return;
        }

        // Get book info for success message
        Book book = bookDAO.findById(transaction.getBookId());
        Member member = memberDAO.findById(transaction.getMemberId());

        // Return the book
        BigDecimal fine = transactionDAO.returnBook(transactionId);

        if (fine.compareTo(BigDecimal.valueOf(-1)) != 0) {
            StringBuilder successMsg = new StringBuilder();
            successMsg.append("Book '").append(book != null ? book.getTitle() : "Unknown")
                    .append("' returned successfully by ").append(member != null ? member.getName() : "Unknown")
                    .append("!");

            if (fine.compareTo(BigDecimal.ZERO) > 0) {
                successMsg.append(" Fine amount: $").append(fine.setScale(2));
            }

            request.setAttribute("success", successMsg.toString());
            request.setAttribute("fineAmount", fine);
        } else {
            request.setAttribute("error", "Failed to process return. Please try again.");
        }

        showReturnPage(request, response);
    }
}
