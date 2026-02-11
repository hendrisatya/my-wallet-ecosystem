package dev.mywallet.ledger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.mywallet.ledger.dto.TopUpRequest;
import dev.mywallet.ledger.entity.WalletEntity;
import dev.mywallet.ledger.service.WalletService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletEntity> createWallet(@RequestBody String userId) {
        // To be: extract userId from the JWT Token here
        return ResponseEntity.ok(walletService.createWallet(userId));
    }

    @PostMapping("/topup")
    public ResponseEntity<WalletEntity> topUp(@RequestBody TopUpRequest request) {
        return ResponseEntity.ok(walletService.topUp(request.getUserId(), request.getAmount()));
    }
}
