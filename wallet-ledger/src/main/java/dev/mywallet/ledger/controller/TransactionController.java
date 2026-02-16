package dev.mywallet.ledger.controller;

import dev.mywallet.ledger.entity.TransactionEntity;
import dev.mywallet.ledger.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<TransactionEntity>> getHistory(
            @PathVariable String userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(userId, page, size));
    }

}
