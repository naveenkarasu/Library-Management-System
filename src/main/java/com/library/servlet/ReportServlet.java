package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.model.Book;
import com.library.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for generating reports.
 * Provides overdue books report and inventory status report.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
@WebServlet(name = "ReportServlet", urlPatterns = {"/reports"})
public class ReportServlet extends HttpServlet {

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

        String reportType = request.getParameter("type");
        if (reportType == null) {
            reportType = "overview";
        }

        try {
            switch (reportType) {
                case "overdue":
                    showOverdueReport(request, response);
                    break;
                case "inventory":
                    showInventoryReport(request, response);
                    break;
                case "issued":
                    showIssuedBooksReport(request, response);
                    break;
                case "overview":
                default:
                    showOverview(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error generating report: " + e.getMessage());
            showOverview(request, response);
        }
    }

    /**
     * Show overview dashboard with statistics
     */
    private void showOverview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get statistics
        int totalBooks = bookDAO.getTotalBooks();
        int totalAvailable = bookDAO.getTotalAvailable();
        int totalIssued = bookDAO.getTotalIssued();
        int totalMembers = memberDAO.getTotalMembers();
        int totalStudents = memberDAO.getCountByType("student");
        int totalFaculty = memberDAO.getCountByType("faculty");
        int activeIssues = transactionDAO.countActive();
        int overdueCount = transactionDAO.countOverdue();

        request.setAttribute("totalBooks", totalBooks);
        request.setAttribute("totalAvailable", totalAvailable);
        request.setAttribute("totalIssued", totalIssued);
        request.setAttribute("totalMembers", totalMembers);
        request.setAttribute("totalStudents", totalStudents);
        request.setAttribute("totalFaculty", totalFaculty);
        request.setAttribute("activeIssues", activeIssues);
        request.setAttribute("overdueCount", overdueCount);
        request.setAttribute("reportType", "overview");

        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }

    /**
     * Show overdue books report
     */
    private void showOverdueReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Transaction> overdueTransactions = transactionDAO.findOverdueWithDetails();
        request.setAttribute("overdueTransactions", overdueTransactions);
        request.setAttribute("reportType", "overdue");

        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }

    /**
     * Show inventory status report
     */
    private void showInventoryReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Book> allBooks = bookDAO.findAll();
        request.setAttribute("allBooks", allBooks);
        request.setAttribute("reportType", "inventory");

        // Calculate totals
        int totalQuantity = 0;
        int totalAvailable = 0;
        int totalIssued = 0;

        for (Book book : allBooks) {
            totalQuantity += book.getQuantity();
            totalAvailable += book.getAvailable();
            totalIssued += (book.getQuantity() - book.getAvailable());
        }

        request.setAttribute("totalQuantity", totalQuantity);
        request.setAttribute("totalAvailable", totalAvailable);
        request.setAttribute("totalIssued", totalIssued);

        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }

    /**
     * Show currently issued books report
     */
    private void showIssuedBooksReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Transaction> issuedTransactions = transactionDAO.findActiveWithDetails();
        request.setAttribute("issuedTransactions", issuedTransactions);
        request.setAttribute("reportType", "issued");

        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }
}
