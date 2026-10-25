package com.martin.risk.engine;

public interface RiskRule {
    RuleNr code();
    boolean matches(Payment payment, ClientContext ctx);
}
