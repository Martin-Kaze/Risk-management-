package com.martin.risk.engine.rules;

import com.martin.risk.engine.ClientContext;
import com.martin.risk.engine.Payment;
import com.martin.risk.engine.RiskRule;
import com.martin.risk.engine.RuleNr;

import java.math.BigDecimal;

public class R4NightLargeRule implements RiskRule {

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("500");

    @Override
    public RuleNr code() {
        return RuleNr.R4_NIGHT_LARGE;
    }

    @Override
    public boolean matches(Payment payment, ClientContext ctx) {
        return payment.timestamp().getHour() < 5
                && payment.amountEur().compareTo(MIN_AMOUNT) >= 0;
    }
}
