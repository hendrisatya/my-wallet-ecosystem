package dev.mywallet.ledger.service;

import dev.mywallet.ledger.entity.TransactionEntity;
import dev.mywallet.ledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Page<TransactionEntity> getTransactionHistory(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
}
