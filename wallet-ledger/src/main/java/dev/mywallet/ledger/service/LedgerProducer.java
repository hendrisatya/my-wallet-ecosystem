package dev.mywallet.ledger.service;

import dev.mywallet.common.event.TransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerProducer {
    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;

    public void sendTransferEvent(TransferEvent event) {
        log.info("Emitting Transfer Event: {}", event);

        Message<TransferEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "transfer-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
