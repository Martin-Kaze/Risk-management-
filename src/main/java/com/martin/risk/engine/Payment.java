package com.martin.risk.engine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Payment(String clientId, BigDecimal amountEur,
                      String recipientId, String countryOrigin, LocalDateTime timestamp) {
}
