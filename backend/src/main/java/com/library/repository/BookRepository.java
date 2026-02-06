package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Book> search(@Param("q") String query);

    List<Book> findByAvailableGreaterThan(int available);

    @Query("SELECT COUNT(b) FROM Book b")
    long countAllBooks();

    @Query("SELECT SUM(b.quantity) FROM Book b")
    Long totalCopies();

    @Query("SELECT SUM(b.available) FROM Book b")
    Long totalAvailable();
}
