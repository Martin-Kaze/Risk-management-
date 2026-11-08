package com.martin.risk.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRequest(@NotBlank String clientId, @NotNull @Positive BigDecimal amount, @NotNull @Pattern(regexp = "EUR|USD|GBP") String currency, @NotBlank String recipientId, @NotBlank String country, @NotNull LocalDateTime timestamp) {
}
