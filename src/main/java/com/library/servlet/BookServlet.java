package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling book management operations.
 * Supports add, edit, delete, search, and list operations.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
@WebServlet(name = "BookServlet", urlPatterns = {"/books"})
public class BookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        bookDAO = new BookDAO();
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
                    deleteBook(request, response);
                    break;
                case "search":
                    searchBooks(request, response);
                    break;
                case "view":
                    viewBook(request, response);
                    break;
                case "list":
                default:
                    listBooks(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            listBooks(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addBook(request, response);
                    break;
                case "update":
                    updateBook(request, response);
                    break;
                default:
                    listBooks(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            listBooks(request, response);
        }
    }

    /**
     * Display list of all books
     */
    private void listBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Book> books = bookDAO.findAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/books.jsp").forward(request, response);
    }

    /**
     * Show form for adding a new book
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "add");
        request.getRequestDispatcher("/books.jsp").forward(request, response);
    }

    /**
     * Show form for editing an existing book
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("id"));
        Book book = bookDAO.findById(bookId);

        if (book != null) {
            request.setAttribute("book", book);
            request.setAttribute("action", "edit");
        } else {
            request.setAttribute("error", "Book not found!");
        }

        List<Book> books = bookDAO.findAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/books.jsp").forward(request, response);
    }

    /**
     * View book details
     */
    private void viewBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("id"));
        Book book = bookDAO.findById(bookId);

        if (book != null) {
            request.setAttribute("viewBook", book);
        } else {
            request.setAttribute("error", "Book not found!");
        }

        List<Book> books = bookDAO.findAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/books.jsp").forward(request, response);
    }

    /**
     * Add a new book
     */
    private void addBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Book book = new Book(isbn, title, author, publisher, quantity);

        if (bookDAO.insert(book)) {
            request.setAttribute("success", "Book added successfully!");
        } else {
            request.setAttribute("error", "Failed to add book. ISBN might already exist.");
        }

        listBooks(request, response);
    }

    /**
     * Update an existing book
     */
    private void updateBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int available = Integer.parseInt(request.getParameter("available"));

        Book book = new Book();
        book.setBookId(bookId);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setQuantity(quantity);
        book.setAvailable(available);

        if (bookDAO.update(book)) {
            request.setAttribute("success", "Book updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update book.");
        }

        listBooks(request, response);
    }

    /**
     * Delete a book
     */
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("id"));

        if (bookDAO.delete(bookId)) {
            request.setAttribute("success", "Book deleted successfully!");
        } else {
            request.setAttribute("error", "Failed to delete book. It might have active transactions.");
        }

        listBooks(request, response);
    }

    /**
     * Search books by keyword
     */
    private void searchBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

        List<Book> books;
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.search(keyword.trim());
            request.setAttribute("keyword", keyword);
        } else {
            books = bookDAO.findAll();
        }

        request.setAttribute("books", books);
        request.getRequestDispatcher("/books.jsp").forward(request, response);
    }
}
