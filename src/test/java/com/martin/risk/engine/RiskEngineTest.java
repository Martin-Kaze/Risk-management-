package com.martin.risk.engine;

import com.martin.risk.engine.rules.R1AmountSpikeRule;
import com.martin.risk.engine.rules.R2NewRecipientRule;
import com.martin.risk.engine.rules.R3MultiCountryRule;
import com.martin.risk.engine.rules.R4NightLargeRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskEngineTest {

    private final RiskEngine engine = new RiskEngine(List.of(
            new R1AmountSpikeRule(), new R2NewRecipientRule(),
            new R3MultiCountryRule(), new R4NightLargeRule()));


    private final Payment nightPayment = new Payment("C2", new BigDecimal("1000"), "R-77", "LT",
            LocalDateTime.of(2026, 11, 10, 3, 0));
    private final ClientContext c2History = new ClientContext(new BigDecimal("300"), 8, Set.of("R-10"), Set.of());

    @Test
    void normalPaymentIsApproved() {
        Payment payment = new Payment("C1", new BigDecimal("20"), "R-12", "LT",
                LocalDateTime.of(2026, 11, 10, 14, 30));
        ClientContext c1History = new ClientContext(new BigDecimal("40"), 10, Set.of("R-12"), Set.of());

        RiskResult result = engine.evaluate(payment, c1History, RiskProfile.defaults());

        assertEquals(Decision.APPROVE, result.decision());
        assertEquals(0, result.score());
        assertEquals(List.of(), result.triggeredRules());
    }

    @Test
    void scoreEqualToReviewThresholdGivesReview() {
        RiskResult result = engine.evaluate(nightPayment, c2History, RiskProfile.defaults());

        assertEquals(Decision.REVIEW, result.decision());
        assertEquals(40, result.score());
        assertEquals(List.of(
                new RuleResult(RuleNr.R2_NEW_RECIPIENT, 25),
                new RuleResult(RuleNr.R4_NIGHT_LARGE, 15)), result.triggeredRules());
    }

    @Test
    void stricterProfileBlocksSamePayment() {
        RiskProfile orgB = new RiskProfile(RiskProfile.DEFAULT_POINTS, 25, 40);

        RiskResult result = engine.evaluate(nightPayment, c2History, orgB);

        assertEquals(Decision.BLOCK, result.decision());
        assertEquals(40, result.score());
    }
}
