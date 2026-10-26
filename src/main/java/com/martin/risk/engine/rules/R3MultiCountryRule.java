package com.martin.risk.engine.rules;

import com.martin.risk.engine.ClientContext;
import com.martin.risk.engine.Payment;
import com.martin.risk.engine.RiskRule;
import com.martin.risk.engine.RuleNr;
import org.springframework.stereotype.Component;

@Component
public class R3MultiCountryRule implements RiskRule {

    @Override
    public RuleNr code() {
        return RuleNr.R3_MULTI_COUNTRY;
    }

    @Override
    public boolean matches(Payment payment, ClientContext ctx) {
        int size =  ctx.countriesLast10Min().contains(payment.countryOrigin())  ? 3 : 2;
        return   ctx.countriesLast10Min().size() >= size;

    }
}
