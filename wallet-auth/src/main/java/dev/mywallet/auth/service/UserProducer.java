package dev.mywallet.auth.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import dev.mywallet.common.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProducer {
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @SuppressWarnings("null")
    public void sendUserCreatedEvent(UserCreatedEvent event) {
        log.info(String.format("Example Log -> Producing user event: %s", event));

        Message<UserCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "user-created-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
