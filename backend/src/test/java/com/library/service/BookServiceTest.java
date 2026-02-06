package com.library.service;

import com.library.dto.BookDto;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;
    private BookDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleBook = new Book("978-0-13-468599-1", "The Pragmatic Programmer",
                "David Thomas", "Addison-Wesley", 3);
        sampleBook.setBookId(1L);

        sampleDto = new BookDto("978-0-13-468599-1", "The Pragmatic Programmer",
                "David Thomas", "Addison-Wesley", 3);
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        Book book2 = new Book("978-0-13-235088-4", "Clean Code", "Robert C. Martin",
                "Prentice Hall", 2);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(sampleBook, book2));

        List<Book> books = bookService.findAll();

        assertEquals(2, books.size());
        verify(bookRepository).findAll();
    }

    @Test
    void findById_shouldReturnBook_whenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        Optional<Book> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("The Pragmatic Programmer", result.get().getTitle());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void search_shouldReturnMatchingBooks() {
        when(bookRepository.search("pragmatic")).thenReturn(List.of(sampleBook));

        List<Book> results = bookService.search("pragmatic");

        assertEquals(1, results.size());
        assertEquals("The Pragmatic Programmer", results.get(0).getTitle());
    }

    @Test
    void search_shouldReturnAll_whenQueryIsEmpty() {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));

        List<Book> results = bookService.search("");

        assertEquals(1, results.size());
        verify(bookRepository).findAll();
        verify(bookRepository, never()).search(anyString());
    }

    @Test
    void createBook_shouldSaveAndReturnBook() {
        when(bookRepository.findByIsbn(sampleDto.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book saved = invocation.getArgument(0);
            saved.setBookId(1L);
            return saved;
        });

        Book created = bookService.createBook(sampleDto);

        assertNotNull(created);
        assertEquals(sampleDto.getIsbn(), created.getIsbn());
        assertEquals(sampleDto.getTitle(), created.getTitle());
        assertEquals(sampleDto.getQuantity(), created.getQuantity());
        assertEquals(sampleDto.getQuantity(), created.getAvailable());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_shouldThrow_whenIsbnExists() {
        when(bookRepository.findByIsbn(sampleDto.getIsbn())).thenReturn(Optional.of(sampleBook));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookService.createBook(sampleDto));

        assertTrue(exception.getMessage().contains("already exists"));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_shouldUpdateAndReturnBook() {
        BookDto updateDto = new BookDto("978-0-13-468599-1", "Updated Title",
                "Updated Author", "Updated Publisher", 5);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.findByIsbn(updateDto.getIsbn())).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updated = bookService.updateBook(1L, updateDto);

        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Updated Author", updated.getAuthor());
        assertEquals(5, updated.getQuantity());
    }

    @Test
    void updateBook_shouldThrow_whenBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> bookService.updateBook(99L, sampleDto));
    }

    @Test
    void updateBook_shouldAdjustAvailable_whenQuantityChanges() {
        sampleBook.setQuantity(3);
        sampleBook.setAvailable(2);
        BookDto updateDto = new BookDto("978-0-13-468599-1", "The Pragmatic Programmer",
                "David Thomas", "Addison-Wesley", 5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.findByIsbn(updateDto.getIsbn())).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updated = bookService.updateBook(1L, updateDto);

        assertEquals(5, updated.getQuantity());
        assertEquals(4, updated.getAvailable());
    }

    @Test
    void deleteBook_shouldDelete_whenNoActiveLoans() {
        sampleBook.setQuantity(3);
        sampleBook.setAvailable(3);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        bookService.deleteBook(1L);

        verify(bookRepository).delete(sampleBook);
    }

    @Test
    void deleteBook_shouldThrow_whenActiveLoansExist() {
        sampleBook.setQuantity(3);
        sampleBook.setAvailable(1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        assertThrows(IllegalStateException.class,
                () -> bookService.deleteBook(1L));
        verify(bookRepository, never()).delete(any(Book.class));
    }

    @Test
    void decrementAvailable_shouldDecrement_whenAvailable() {
        sampleBook.setAvailable(2);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        bookService.decrementAvailable(sampleBook);

        assertEquals(1, sampleBook.getAvailable());
        verify(bookRepository).save(sampleBook);
    }

    @Test
    void decrementAvailable_shouldThrow_whenNoneAvailable() {
        sampleBook.setAvailable(0);

        assertThrows(IllegalStateException.class,
                () -> bookService.decrementAvailable(sampleBook));
    }

    @Test
    void incrementAvailable_shouldIncrement() {
        sampleBook.setAvailable(1);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        bookService.incrementAvailable(sampleBook);

        assertEquals(2, sampleBook.getAvailable());
        verify(bookRepository).save(sampleBook);
    }
}
