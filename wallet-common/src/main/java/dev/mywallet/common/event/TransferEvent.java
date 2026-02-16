package dev.mywallet.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TransferEvent {
    private String transactionId;
    private String fromUserId;
    private String toUserId;
    private BigDecimal amount;
    private String status; // SUCCESS, FAILED
}
