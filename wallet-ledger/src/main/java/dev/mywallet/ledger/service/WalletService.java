package dev.mywallet.ledger.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import dev.mywallet.ledger.entity.WalletEntity;
import dev.mywallet.ledger.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository repository;

    @SuppressWarnings("null")
    public WalletEntity createWallet(String userId) {
        // prevent duplicates
        if (repository.existsByUserId(userId)) {
            throw new RuntimeException("Wallet already exists for user: " + userId);
        }

        WalletEntity wallet = WalletEntity.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .currency("IDR")
                .build();
        
        return repository.save(wallet);
    }
}
