package dev.mywallet.ledger.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.mywallet.ledger.entity.TransactionEntity;
import dev.mywallet.ledger.entity.WalletEntity;
import dev.mywallet.ledger.repository.TransactionRepository;
import dev.mywallet.ledger.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @SuppressWarnings("null")
    public WalletEntity createWallet(String userId) {
        // prevent duplicates
        if (walletRepository.existsByUserId(userId)) {
            throw new RuntimeException("Wallet already exists for user: " + userId);
        }

        WalletEntity wallet = WalletEntity.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .currency("IDR")
                .build();
        
        return walletRepository.save(wallet);
    }

    @Transactional
    @SuppressWarnings("null")
    public WalletEntity topUp(String userId, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findByUserIdAndCurrency(userId, "IDR")
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        TransactionEntity transaction = TransactionEntity.builder()
                .userId(userId)
                .amount(amount)
                .transactionType("TOPUP")
                .referenceId(UUID.randomUUID().toString()) // Currently only mocking a payment gateway ID. 
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);

        return wallet;

    }
}
