package dev.mywallet.notification.service;

import dev.mywallet.common.event.TransferEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "transfer-topic", groupId = "notification-group")
    public void handleTransferEvent(TransferEvent event) {
        log.info("=================================================");
        log.info(" [NOTIFICATION SERVICE] EMAIL SENT");
        log.info("-------------------------------------------------");
        log.info(" Transaction ID: {}", event.getTransactionId());
        log.info(" From User:      {}", event.getFromUserId());
        log.info(" To User:        {}", event.getToUserId());
        log.info(" Amount:         Rp. {}", event.getAmount());
        log.info(" Status:         {}", event.getStatus());
        log.info("=================================================");
    }
}
