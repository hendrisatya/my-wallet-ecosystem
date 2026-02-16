package dev.mywallet.ledger.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    String fromUserId;
    String toUserId;
    BigDecimal amount;
}
