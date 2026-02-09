package dev.mywallet.ledger.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.mywallet.ledger.entity.WalletEntity;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
