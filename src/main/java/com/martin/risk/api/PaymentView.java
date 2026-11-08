package com.martin.risk.api;

import com.martin.risk.data.PaymentRecord;
import com.martin.risk.engine.Decision;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentView(Long id, String clientId, BigDecimal amountEur, LocalDateTime timestamp,
                          Decision decision, int score) {

    public static PaymentView from(PaymentRecord record) {
        return new PaymentView(record.getId(), record.getClientId(), record.getAmountEur(),
                record.getTimestamp(), record.getDecision(), record.getScore());
    }
}
