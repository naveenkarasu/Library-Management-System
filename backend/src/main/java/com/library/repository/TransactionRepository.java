package com.library.repository;

import com.library.model.Transaction;
import com.library.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStatus(TransactionStatus status);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.book JOIN FETCH t.member WHERE t.status = :status")
    List<Transaction> findByStatusWithDetails(@Param("status") TransactionStatus status);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.book JOIN FETCH t.member " +
           "WHERE t.status = 'ISSUED' AND t.dueDate < :date")
    List<Transaction> findOverdueTransactions(@Param("date") LocalDate date);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.book JOIN FETCH t.member " +
           "WHERE t.member.memberId = :memberId AND t.status = :status")
    List<Transaction> findByMemberAndStatus(@Param("memberId") Long memberId,
                                            @Param("status") TransactionStatus status);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.member.memberId = :memberId AND t.status = 'ISSUED'")
    long countActiveByMember(@Param("memberId") Long memberId);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.book JOIN FETCH t.member ORDER BY t.createdAt DESC")
    List<Transaction> findAllWithDetails();
}
