package dev.mywallet.ledger.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import dev.mywallet.common.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {
    private final WalletService walletService;

    @KafkaListener(topics = "user-created-topic", groupId = "ledger-group")
    public void consume(UserCreatedEvent event) {
        log.info(String.format("Example Log -> Consumed user event: %s", event));

        walletService.createWallet(event.getUserId());
    }
}
