package com.library.service;

import com.library.dto.DashboardStats;
import com.library.model.Transaction;
import com.library.model.TransactionStatus;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    public ReportService(BookRepository bookRepository,
                         MemberRepository memberRepository,
                         TransactionRepository transactionRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
    }

    public DashboardStats getDashboardStats() {
        long totalBooks = bookRepository.countAllBooks();
        long totalMembers = memberRepository.count();

        List<Transaction> activeTransactions =
                transactionRepository.findByStatus(TransactionStatus.ISSUED);
        long activeCount = activeTransactions.size();

        List<Transaction> overdueTransactions =
                transactionRepository.findOverdueTransactions(LocalDate.now());
        long overdueCount = overdueTransactions.size();

        Long totalCopies = bookRepository.totalCopies();
        Long availableCopies = bookRepository.totalAvailable();

        return new DashboardStats(
                totalBooks,
                totalMembers,
                activeCount,
                overdueCount,
                totalCopies != null ? totalCopies : 0,
                availableCopies != null ? availableCopies : 0
        );
    }

    public List<Transaction> getOverdueBooks() {
        return transactionRepository.findOverdueTransactions(LocalDate.now());
    }
}
