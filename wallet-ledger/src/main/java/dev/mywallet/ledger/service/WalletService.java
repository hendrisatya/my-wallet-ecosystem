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

    @Transactional
    public void transfer(String fromUserId, String toUserId, BigDecimal amount) {
        // 1. Validation
        if (fromUserId.equals(toUserId)) {
            throw new RuntimeException("Cannot transfer to self");
        }

        // 2. Deadlock Prevention: Order the IDs
        String firstLock = fromUserId.compareTo(toUserId) < 0 ? fromUserId : toUserId;
        String secondLock = fromUserId.compareTo(toUserId) < 0 ? toUserId : fromUserId;

        // 3. Acquire Locks in Order
        WalletEntity senderWallet = walletRepository.findByUserIdAndCurrency(fromUserId, "IDR")
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));

        WalletEntity receiverWallet = walletRepository.findByUserIdAndCurrency(toUserId, "IDR")
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        // 4. Check Balance
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 5. Move Money
        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        // 6. Record Transaction (Sender)
        String refId = UUID.randomUUID().toString();

        TransactionEntity senderTx = TransactionEntity.builder()
                .userId(fromUserId)
                .amount(amount.negate()) // Negative for sender
                .transactionType("TRANSFER_OUT")
                .referenceId(refId)
                .status("SUCCESS")
                .build();

        // 7. Record Transaction (Receiver)
        TransactionEntity receiverTx = TransactionEntity.builder()
                .userId(fromUserId)
                .amount(amount) // Positive for receiver
                .transactionType("TRANSFER_IN")
                .referenceId(refId)
                .status("SUCCESS")
                .build();

        transactionRepository.save(senderTx);
        transactionRepository.save(receiverTx);
    }
}
