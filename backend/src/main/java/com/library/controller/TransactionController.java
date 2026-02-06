package com.library.controller;

import com.library.dto.IssueBookRequest;
import com.library.dto.ReturnBookResponse;
import com.library.model.Transaction;
import com.library.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "Book issue and return endpoints")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @Operation(summary = "Get all transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active (issued) transactions")
    public ResponseEntity<List<Transaction>> getActiveTransactions() {
        return ResponseEntity.ok(transactionService.findActive());
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue transactions")
    public ResponseEntity<List<Transaction>> getOverdueTransactions() {
        return ResponseEntity.ok(transactionService.findOverdue());
    }

    @PostMapping("/issue")
    @Operation(summary = "Issue a book to a member")
    public ResponseEntity<Object> issueBook(@Valid @RequestBody IssueBookRequest request) {
        try {
            Transaction transaction = transactionService.issueBook(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Return a book")
    public ResponseEntity<Object> returnBook(@PathVariable Long id) {
        try {
            ReturnBookResponse response = transactionService.returnBook(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
