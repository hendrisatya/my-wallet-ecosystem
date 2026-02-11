package dev.mywallet.ledger.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import dev.mywallet.ledger.entity.WalletEntity;
import jakarta.persistence.LockModeType;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByUserId(String userId);

    boolean existsByUserId(String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // <--- Freezes the row during update
    Optional<WalletEntity> findByUserIdAndCurrency(String userId, String currency);
}
