package com.library.service;

import com.library.dto.BookDto;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.search(query.trim());
    }

    public List<Book> findAvailable() {
        return bookRepository.findByAvailableGreaterThan(0);
    }

    public Book createBook(BookDto dto) {
        Optional<Book> existing = bookRepository.findByIsbn(dto.getIsbn());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("A book with ISBN " + dto.getIsbn() + " already exists");
        }

        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setQuantity(dto.getQuantity());
        book.setAvailable(dto.getQuantity());
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));

        Optional<Book> existingWithIsbn = bookRepository.findByIsbn(dto.getIsbn());
        if (existingWithIsbn.isPresent() && !existingWithIsbn.get().getBookId().equals(id)) {
            throw new IllegalArgumentException("Another book with ISBN " + dto.getIsbn() + " already exists");
        }

        int quantityDiff = dto.getQuantity() - book.getQuantity();
        int newAvailable = book.getAvailable() + quantityDiff;
        if (newAvailable < 0) {
            throw new IllegalArgumentException(
                    "Cannot reduce quantity below the number of currently issued copies");
        }

        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setQuantity(dto.getQuantity());
        book.setAvailable(newAvailable);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));

        if (book.getAvailable() < book.getQuantity()) {
            throw new IllegalStateException("Cannot delete book with active loans");
        }

        bookRepository.delete(book);
    }

    public void decrementAvailable(Book book) {
        if (book.getAvailable() <= 0) {
            throw new IllegalStateException("No available copies of: " + book.getTitle());
        }
        book.setAvailable(book.getAvailable() - 1);
        bookRepository.save(book);
    }

    public void incrementAvailable(Book book) {
        book.setAvailable(book.getAvailable() + 1);
        bookRepository.save(book);
    }
}
