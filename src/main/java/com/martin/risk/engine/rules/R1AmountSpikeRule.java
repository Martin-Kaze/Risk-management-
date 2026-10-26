package com.martin.risk.engine.rules;

import com.martin.risk.engine.ClientContext;
import com.martin.risk.engine.Payment;
import com.martin.risk.engine.RiskRule;
import com.martin.risk.engine.RuleNr;

import java.math.BigDecimal;

public class R1AmountSpikeRule implements RiskRule {
    private static final int MIN_PAYMENTS = 5;
    private static final BigDecimal SPIKE_FACTOR = new BigDecimal("5");

    @Override
    public RuleNr code() {
        return RuleNr.R1_AMOUNT_SPIKE;
    }

    @Override
    public boolean matches(Payment payment, ClientContext ctx) {

        return payment.amountEur().compareTo(ctx.avg30d().multiply(SPIKE_FACTOR)) > 0
                && ctx.count30d() >= MIN_PAYMENTS;
    }
}
