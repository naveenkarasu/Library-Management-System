package com.library.service;

import com.library.dto.IssueBookRequest;
import com.library.dto.ReturnBookResponse;
import com.library.model.*;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private TransactionService transactionService;

    private Book sampleBook;
    private Member studentMember;
    private Member facultyMember;

    @BeforeEach
    void setUp() {
        sampleBook = new Book("978-0-13-468599-1", "The Pragmatic Programmer",
                "David Thomas", "Addison-Wesley", 3);
        sampleBook.setBookId(1L);
        sampleBook.setAvailable(3);

        studentMember = new Member("Alice Johnson", "alice@university.edu",
                "555-0101", MemberType.STUDENT);
        studentMember.setMemberId(1L);

        facultyMember = new Member("Dr. Brown", "brown@university.edu",
                "555-0201", MemberType.FACULTY);
        facultyMember.setMemberId(2L);
    }

    @Test
    void issueBook_shouldCreateTransaction_forStudent() {
        IssueBookRequest request = new IssueBookRequest(1L, 1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(studentMember));
        when(transactionRepository.countActiveByMember(1L)).thenReturn(0L);
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> {
                    Transaction t = invocation.getArgument(0);
                    t.setTransactionId(1L);
                    return t;
                });

        Transaction result = transactionService.issueBook(request);

        assertNotNull(result);
        assertEquals(sampleBook, result.getBook());
        assertEquals(studentMember, result.getMember());
        assertEquals(LocalDate.now(), result.getIssueDate());
        assertEquals(LocalDate.now().plusDays(14), result.getDueDate());
        assertEquals(TransactionStatus.ISSUED, result.getStatus());
        verify(bookService).decrementAvailable(sampleBook);
    }

    @Test
    void issueBook_shouldCreateTransaction_forFaculty() {
        IssueBookRequest request = new IssueBookRequest(1L, 2L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(facultyMember));
        when(transactionRepository.countActiveByMember(2L)).thenReturn(0L);
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> {
                    Transaction t = invocation.getArgument(0);
                    t.setTransactionId(1L);
                    return t;
                });

        Transaction result = transactionService.issueBook(request);

        assertEquals(LocalDate.now().plusDays(30), result.getDueDate());
    }

    @Test
    void issueBook_shouldThrow_whenBookNotAvailable() {
        sampleBook.setAvailable(0);
        IssueBookRequest request = new IssueBookRequest(1L, 1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(studentMember));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> transactionService.issueBook(request));

        assertTrue(exception.getMessage().contains("not available"));
        verify(bookService, never()).decrementAvailable(any());
    }

    @Test
    void issueBook_shouldThrow_whenStudentExceedsMaxBooks() {
        IssueBookRequest request = new IssueBookRequest(1L, 1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(studentMember));
        when(transactionRepository.countActiveByMember(1L)).thenReturn(5L);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> transactionService.issueBook(request));

        assertTrue(exception.getMessage().contains("maximum"));
        assertTrue(exception.getMessage().contains("5"));
    }

    @Test
    void issueBook_shouldThrow_whenFacultyExceedsMaxBooks() {
        IssueBookRequest request = new IssueBookRequest(1L, 2L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(facultyMember));
        when(transactionRepository.countActiveByMember(2L)).thenReturn(10L);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> transactionService.issueBook(request));

        assertTrue(exception.getMessage().contains("maximum"));
        assertTrue(exception.getMessage().contains("10"));
    }

    @Test
    void issueBook_shouldThrow_whenBookNotFound() {
        IssueBookRequest request = new IssueBookRequest(99L, 1L);
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.issueBook(request));
    }

    @Test
    void issueBook_shouldThrow_whenMemberNotFound() {
        IssueBookRequest request = new IssueBookRequest(1L, 99L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.issueBook(request));
    }

    @Test
    void returnBook_shouldReturnWithNoFine_whenNotOverdue() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setBook(sampleBook);
        transaction.setMember(studentMember);
        transaction.setIssueDate(LocalDate.now().minusDays(5));
        transaction.setDueDate(LocalDate.now().plusDays(9));
        transaction.setStatus(TransactionStatus.ISSUED);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReturnBookResponse response = transactionService.returnBook(1L);

        assertEquals(TransactionStatus.RETURNED, response.getTransaction().getStatus());
        assertEquals(BigDecimal.ZERO, response.getFineAmount());
        assertEquals(0, response.getOverdueDays());
        verify(bookService).incrementAvailable(sampleBook);
    }

    @Test
    void returnBook_shouldCalculateFine_whenOverdue3Days() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setBook(sampleBook);
        transaction.setMember(studentMember);
        transaction.setIssueDate(LocalDate.now().minusDays(17));
        transaction.setDueDate(LocalDate.now().minusDays(3));
        transaction.setStatus(TransactionStatus.ISSUED);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReturnBookResponse response = transactionService.returnBook(1L);

        assertEquals(TransactionStatus.RETURNED, response.getTransaction().getStatus());
        assertEquals(new BigDecimal("1.50"), response.getFineAmount());
        assertEquals(3, response.getOverdueDays());
    }

    @Test
    void returnBook_shouldCalculateFine_whenOverdue10Days() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setBook(sampleBook);
        transaction.setMember(studentMember);
        transaction.setIssueDate(LocalDate.now().minusDays(24));
        transaction.setDueDate(LocalDate.now().minusDays(10));
        transaction.setStatus(TransactionStatus.ISSUED);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReturnBookResponse response = transactionService.returnBook(1L);

        assertEquals(new BigDecimal("5.00"), response.getFineAmount());
        assertEquals(10, response.getOverdueDays());
    }

    @Test
    void returnBook_shouldCalculateFine_atHalfDollarPerDay() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setBook(sampleBook);
        transaction.setMember(studentMember);
        transaction.setIssueDate(LocalDate.now().minusDays(21));
        transaction.setDueDate(LocalDate.now().minusDays(7));
        transaction.setStatus(TransactionStatus.ISSUED);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReturnBookResponse response = transactionService.returnBook(1L);

        BigDecimal expectedFine = new BigDecimal("0.50").multiply(BigDecimal.valueOf(7));
        assertEquals(expectedFine, response.getFineAmount());
        assertEquals(7, response.getOverdueDays());
    }

    @Test
    void returnBook_shouldThrow_whenAlreadyReturned() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setStatus(TransactionStatus.RETURNED);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> transactionService.returnBook(1L));

        assertTrue(exception.getMessage().contains("already been returned"));
    }

    @Test
    void returnBook_shouldThrow_whenTransactionNotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.returnBook(99L));
    }

    @Test
    void findOverdue_shouldReturnOverdueTransactions() {
        Transaction overdueTransaction = new Transaction();
        overdueTransaction.setTransactionId(1L);
        overdueTransaction.setDueDate(LocalDate.now().minusDays(5));
        overdueTransaction.setStatus(TransactionStatus.ISSUED);

        when(transactionRepository.findOverdueTransactions(LocalDate.now()))
                .thenReturn(List.of(overdueTransaction));

        List<Transaction> overdueList = transactionService.findOverdue();

        assertEquals(1, overdueList.size());
    }

    @Test
    void fineRate_shouldBeHalfDollarPerDay() {
        assertEquals(new BigDecimal("0.50"), Transaction.FINE_PER_DAY);
    }

    @Test
    void studentLoanPeriod_shouldBe14Days() {
        assertEquals(14, MemberType.STUDENT.getLoanPeriodDays());
    }

    @Test
    void facultyLoanPeriod_shouldBe30Days() {
        assertEquals(30, MemberType.FACULTY.getLoanPeriodDays());
    }

    @Test
    void studentMaxBooks_shouldBe5() {
        assertEquals(5, MemberType.STUDENT.getMaxBooks());
    }

    @Test
    void facultyMaxBooks_shouldBe10() {
        assertEquals(10, MemberType.FACULTY.getMaxBooks());
    }
}
