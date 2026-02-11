package dev.mywallet.ledger.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TopUpRequest {
    private String userId;
    private BigDecimal amount;
}
