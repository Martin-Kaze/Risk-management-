package com.martin.risk.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class CurrencyConverter {

    private static final Map<String, BigDecimal> RATES_TO_EUR = Map.of(
            "EUR", BigDecimal.ONE,
            "USD", new BigDecimal("0.92"),
            "GBP", new BigDecimal("1.17"));

    public BigDecimal toEur(BigDecimal amount, String currency) {
        BigDecimal rate = RATES_TO_EUR.get(currency);
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
