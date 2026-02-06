package com.library.service;

import com.library.dto.IssueBookRequest;
import com.library.dto.ReturnBookResponse;
import com.library.model.*;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BookService bookService;

    public TransactionService(TransactionRepository transactionRepository,
                              BookRepository bookRepository,
                              MemberRepository memberRepository,
                              BookService bookService) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.bookService = bookService;
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAllWithDetails();
    }

    public List<Transaction> findActive() {
        return transactionRepository.findByStatusWithDetails(TransactionStatus.ISSUED);
    }

    public List<Transaction> findOverdue() {
        return transactionRepository.findOverdueTransactions(LocalDate.now());
    }

    public Transaction issueBook(IssueBookRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + request.getBookId()));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + request.getMemberId()));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book '" + book.getTitle() + "' is not available for lending");
        }

        long activeLoans = transactionRepository.countActiveByMember(member.getMemberId());
        if (activeLoans >= member.getMaxBooks()) {
            throw new IllegalStateException(
                    "Member has reached the maximum number of borrowed books (" +
                    member.getMaxBooks() + " for " + member.getMemberType().name().toLowerCase() + ")");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(member.getLoanPeriodDays());

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setIssueDate(issueDate);
        transaction.setDueDate(dueDate);
        transaction.setStatus(TransactionStatus.ISSUED);
        transaction.setFineAmount(BigDecimal.ZERO);

        bookService.decrementAvailable(book);

        return transactionRepository.save(transaction);
    }

    public ReturnBookResponse returnBook(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + transactionId));

        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            throw new IllegalStateException("This book has already been returned");
        }

        LocalDate returnDate = LocalDate.now();
        transaction.setReturnDate(returnDate);
        transaction.setStatus(TransactionStatus.RETURNED);

        BigDecimal fineAmount = BigDecimal.ZERO;
        long overdueDays = 0;

        if (returnDate.isAfter(transaction.getDueDate())) {
            overdueDays = ChronoUnit.DAYS.between(transaction.getDueDate(), returnDate);
            fineAmount = Transaction.FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
        }

        transaction.setFineAmount(fineAmount);

        bookService.incrementAvailable(transaction.getBook());

        Transaction saved = transactionRepository.save(transaction);

        return new ReturnBookResponse(saved, fineAmount, overdueDays);
    }

    public List<Transaction> findByMember(Long memberId, TransactionStatus status) {
        return transactionRepository.findByMemberAndStatus(memberId, status);
    }
}
