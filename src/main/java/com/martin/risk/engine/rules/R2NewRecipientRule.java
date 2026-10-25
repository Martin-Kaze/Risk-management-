package com.martin.risk.engine.rules;

import com.martin.risk.engine.ClientContext;
import com.martin.risk.engine.Payment;
import com.martin.risk.engine.RiskRule;
import com.martin.risk.engine.RuleNr;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class R2NewRecipientRule implements RiskRule {
    private static final BigDecimal MIN_AMOUNT = new BigDecimal("1000");

    @Override
    public RuleNr code() {
        return RuleNr.R2_NEW_RECIPIENT;
    }

    @Override
    public boolean matches(Payment payment, ClientContext ctx) {
        return !ctx.knownRecipients90d().contains(payment.recipientId()) && payment.amountEur().compareTo(MIN_AMOUNT) >= 0;

    }
}
